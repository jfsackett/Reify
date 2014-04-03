(ns pal.core)

;    Palletizer
;    Copyright (C) 2014  Sackett Inc.
;
;    This program is free software: you can redistribute it and/or modify
;    it under the terms of the GNU General Public License as published by
;    the Free Software Foundation, either version 3 of the License, or
;    (at your option) any later version.
;
;    This program is distributed in the hope that it will be useful,
;    but WITHOUT ANY WARRANTY; without even the implied warranty of
;    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;    GNU General Public License for more details.
;
;    You should have received a copy of the GNU General Public License
;    along with this program.  If not, see <http://www.gnu.org/licenses/>.

; Item id constants.
(def ids [0x8B0000, 0xA52A2A, 0xB22222, 0xDC143C, 0xFF0000, 0xFF6347, 0xFF7F50, 0xCD5C5C, 0xF08080, 0xE9967A, 0xFA8072, 0xFFA07A, 0xFF4500, 0xFF8C00, 0xFFA500, 0xFFD700, 0xB8860B, 0xDAA520, 0xEEE8AA, 0xBDB76B, 0xF0E68C, 0x808000, 0xFFFF00, 0x9ACD32, 0x556B2F, 0x6B8E23, 0x7CFC00, 0x7FFF00, 0xADFF2F, 0x006400, 0x008000, 0x228B22, 0x00FF00, 0x32CD32, 0x90EE90, 0x98FB98, 0x8FBC8F, 0x00FA9A, 0x00FF7F, 0x2E8B57, 0x66CDAA, 0x3CB371, 0x20B2AA, 0x2F4F4F, 0x008080, 0x008B8B, 0x00FFFF, 0x00FFFF, 0xE0FFFF, 0x00CED1, 0x40E0D0, 0x48D1CC, 0xAFEEEE, 0x7FFFD4, 0xB0E0E6, 0x5F9EA0, 0x4682B4, 0x6495ED, 0x00BFFF, 0x1E90FF, 0xADD8E6, 0x87CEEB, 0x87CEFA, 0x191970, 0x000080, 0x00008B, 0x0000CD, 0x0000FF, 0x4169E1, 0x8A2BE2, 0x4B0082, 0x483D8B, 0x6A5ACD, 0x7B68EE, 0x9370DB, 0x8B008B, 0x9400D3, 0x9932CC, 0xBA55D3, 0x800080, 0xD8BFD8, 0xDDA0DD, 0xEE82EE, 0xFF00FF, 0xDA70D6, 0xC71585, 0xDB7093, 0xFF1493, 0xFF69B4, 0xFFB6C1, 0xFFC0CB])

; Item ids; also used as their color constants.
(def avail-ids (ref (shuffle ids)))

; Pallet dimensions.
(def PALLET_LENGTH 750)
(def PALLET_WIDTH 500)

(defn make-pallet-builder
  "Create a pallet builder."
  [length width]
  (fn []
    {:length length  
     :width width
     :items []}))

; Main pallet builder.
(def pallet-builder (make-pallet-builder PALLET_LENGTH PALLET_WIDTH))

(defn make-item-builder
  "Create an item."
  [avail-ids]
  (fn []
    (dosync 
      (let [id (peek @avail-ids)]
        (alter avail-ids pop)
        {:item {:id id
         :length (* 50 (inc (rand-int 6)))  
         :width (* 50 (inc (rand-int 6)))}}))))

; Main item builder.
(def item-builder (make-item-builder avail-ids))

(defn build-items
  "Build a list of items."
  [num-items]
  (loop [ix num-items
         items []]
    (if (zero? ix) items
      (recur  (dec ix) (conj items (item-builder))))))

(defn build-empty-space-map 
  "Init map for available space in a pallet."
  []
  (loop [width PALLET_WIDTH empty-space-map []]
    (if (zero? width)
      empty-space-map
      (recur (dec width)
             (conj empty-space-map 
                   (loop [length PALLET_LENGTH empty-row []]
                     (if (zero? length)
                       empty-row
                       (recur (dec length)
                              (conj empty-row false)))))))))

; Fresh copy of items.
(def all-items (ref (build-items 15)))

; Unpacked items.
(def unpacked-items (ref (shuffle @all-items)))

; Pallet.
(def pallet (ref (pallet-builder)))

; Space map; keeps track of filled area of pallet.
(def space-map (ref (build-empty-space-map)))

; Unpacked items accessor.
(defn get-unpacked-items [] @unpacked-items)

; Pallet accessor.
(defn get-pallet [] @pallet)

(defn reset-all
  "Clears pallet & generates new items."
  []
  (dosync
    (ref-set all-items (build-items 14))
    (ref-set unpacked-items (shuffle @all-items))
    (ref-set pallet (pallet-builder))
    (ref-set space-map (build-empty-space-map))))

(defn shuffle-items
  "Clears pallet & shuffles existing items."
  []
  (dosync
    (ref-set unpacked-items (shuffle @all-items))
    (ref-set pallet (pallet-builder))
    (ref-set space-map (build-empty-space-map))))

(defn fill-space-map 
  "Fill map of occupied space in a pallet space map."
  []
  (dosync
	  (doseq [item (@pallet :items)]
		  (loop [y (+ ((item :item) :width) (item :y) -1)]
		    (if (>= y (item :y))
	       (do
          (loop [x (+ ((item :item) :length) (item :x) -1)]
	          (if (>= x (item :x))
	            (do 
	              (ref-set space-map (assoc @space-map y (assoc (@space-map y) x true)))
	              (recur (dec x)))))
          (recur (dec y))))))))

(defn find-extents [x-offset y-offset]
  "Find the length & width of the space starting at offsets."
  (let [x-extent
        (loop [x x-offset]
          (if (or (= x PALLET_LENGTH) ((@space-map y-offset) x))
            x
            (recur (inc x))))
        y-extent
        (loop [y y-offset]
          (if (or (= y PALLET_WIDTH) ((@space-map y) x-offset))
            y
            (recur (inc y))))]
    {:x-offset x-offset :y-offset y-offset :length (- x-extent x-offset) :width (- y-extent y-offset)}))

(defn contains-point
  "Clojure checks whether a point is contained in a bin-space."
  [bin-space x y]
  (and (<= (bin-space :x-offset) x)
       (<= x (+ (bin-space :x-offset) (bin-space :length)))
       (<= (bin-space :y-offset) y)
       (<= y (+ (bin-space :y-offset) (bin-space :width)))))
       
(defn contains-space
  "Clojure checks whether new-bin-space contained in bin-space."
  [new-bin-space]
  (fn [bin-space]
    (and (contains-point bin-space (new-bin-space :x-offset) (new-bin-space :y-offset))
         (contains-point bin-space 
                         (+ (new-bin-space :x-offset) (new-bin-space :length))
                         (+ (new-bin-space :y-offset) (new-bin-space :width))))))

(defn set-walls 
    "Sets the wall heights in new bin space."
    [new-bin-space]
    (let [left-wall (if (zero? (new-bin-space :x-offset))
                      (- PALLET_WIDTH (new-bin-space :y-offset))
                      (loop [x-wall (dec (new-bin-space :x-offset)) left-wall (new-bin-space :y-offset)]
                         (if (or (= left-wall PALLET_WIDTH) (not ((@space-map left-wall) x-wall)))
                          (- left-wall (new-bin-space :y-offset))
                          (recur x-wall (inc left-wall)))))
          right-wall (if (= (+ (new-bin-space :x-offset) (new-bin-space :length) PALLET_LENGTH))
                      (- PALLET_WIDTH (new-bin-space :y-offset))
                      (loop [x-wall (inc (+ (new-bin-space :x-offset) (new-bin-space :length))) right-wall (new-bin-space :y-offset)]
                        (if (or (= right-wall PALLET_WIDTH) (not ((@space-map right-wall) x-wall)))
                          (- right-wall (new-bin-space :y-offset))
                          (recur x-wall (inc right-wall)))))]
      (assoc-in (assoc-in new-bin-space [:left-wall] left-wall) [:right-wall] right-wall)))

(defn find-next-space 
  "Find the next available space in the pallet space map."
  [prior-spaces]
  (loop [y-offset (if (empty? prior-spaces) 0 ((peek prior-spaces) :y-offset))
         x-offset-init (if (empty? prior-spaces) 0 (+ ((peek prior-spaces) :x-offset) ((peek prior-spaces) :length)))]
    (if (< y-offset (dec PALLET_WIDTH))
      (let [next-space 
            (loop [x-offset x-offset-init]
              (if (>= x-offset PALLET_LENGTH)
                nil
                (if (not ((@space-map y-offset) x-offset))
                  (let [new-bin-space (find-extents x-offset y-offset)]
                    (if (empty? (filter (contains-space new-bin-space) prior-spaces))
                      (set-walls new-bin-space)
                      (recur (inc x-offset))))
                  (recur (inc x-offset)))))]
        (if-not (nil? next-space)
          next-space
          (recur (inc y-offset) 0))))))

(defn item-fits
  "Determines whether an item fits in the bin space."
  [item bin-space]
  (and (<= ((item :item) :length) (bin-space :length)) (<= ((item :item) :width) (bin-space :width))))

(defn find-best-fit-item-ix
  "Finds the best fitting unpacked item for input space."
  [bin-space]
  (loop [ix 0]
    (if (= (count @unpacked-items) ix)
      -1
      (let [item (@unpacked-items ix)]
        (if (item-fits item bin-space)
          ix
          (recur (inc ix)))))))
        
(defn pack-item
  "Packs an item into pallet at the input coordinates."
  [item x y]
  (alter pallet update-in [:items] conj (assoc-in (assoc-in item [:x] x) [:y] y)))

(defn remove-item
  "Remove an item from a vector & return vector."
  [vector ix]
  (if (zero? (count vector))
    []
    (if (= (inc ix) (count vector))
      (into [] (subvec vector 0 ix))
      (into [] (concat (subvec vector 0 ix) (subvec vector (inc ix)))))))

(defn pack-items
  "Palletizes the unpacked items."
  []
  (dosync
    (loop [prior-spaces [] next-space (find-next-space [])]
      (if (or (empty? @unpacked-items) (nil? next-space))
        @pallet
        (let [best-fit-item-ix (find-best-fit-item-ix next-space)]
          (if (>= best-fit-item-ix 0)
            (let [best-fit-item (@unpacked-items best-fit-item-ix)]
              (ref-set unpacked-items (remove-item @unpacked-items best-fit-item-ix))
              (pack-item best-fit-item (next-space :x-offset) (next-space :y-offset))
              (fill-space-map)
              (recur [] (find-next-space [])))
            (recur (conj prior-spaces next-space) (find-next-space (conj prior-spaces next-space)))))))))

