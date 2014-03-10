(ns pal.core)

;(def bin {:length 750
;          :width 500
;          :items []})
;
;
;(defn make-bin-printer
;  "Print a bin & contents."
;  [bin]
;  (fn []
;    (println (bin :items))))
;
;(def bin-printer (make-bin-printer bin))
;
;(bin-printer)
;
;(def bin-pal (agent bin))
;@bin-pal
;(send bin-pal assoc-in [:items] 'joe)
;(send bin-pal assoc-in [:items] [1])
;(@bin-pal :items)
;(send bin-pal assoc-in [:items] (get-in bin-pal [:items]))
;(send bin-pal assoc-in [:items] (cons 2 (@bin-pal :items)))
;
;(def bin-pal (atom bin))
;
;(defn show-items
;  [bin]
;  (@bin :items))
;
;(defn add-item 
;  "Add an item to a bin."
;  [bin item]
;  (swap! bin assoc-in [:items] (conj (@bin :items) item)))
;
;(add-item bin-pal 'joe)
;(show-items bin-pal)
;(conj (@bin-pal :items) 'joe)
;
;(def item-id-ix (atom 0))
;
;(defn make-item-builder
;  "Create an item."
;  [id-ix]
;  (fn []
;    (swap! id-ix inc)
;    {:id @id-ix
;     :length 100 
;     :width 500}))
;
;(def item-maker (make-item-builder item-id-ix))
;
;(item-maker)

(def ids [0x8B0000, 0xA52A2A, 0xB22222, 0xDC143C, 0xFF0000, 0xFF6347, 0xFF7F50, 0xCD5C5C, 0xF08080, 0xE9967A, 0xFA8072, 0xFFA07A, 0xFF4500, 0xFF8C00, 0xFFA500, 0xFFD700, 0xB8860B, 0xDAA520, 0xEEE8AA, 0xBDB76B, 0xF0E68C, 0x808000, 0xFFFF00, 0x9ACD32, 0x556B2F, 0x6B8E23, 0x7CFC00, 0x7FFF00, 0xADFF2F, 0x006400, 0x008000, 0x228B22, 0x00FF00, 0x32CD32, 0x90EE90, 0x98FB98, 0x8FBC8F, 0x00FA9A, 0x00FF7F, 0x2E8B57, 0x66CDAA, 0x3CB371, 0x20B2AA, 0x2F4F4F, 0x008080, 0x008B8B, 0x00FFFF, 0x00FFFF, 0xE0FFFF, 0x00CED1, 0x40E0D0, 0x48D1CC, 0xAFEEEE, 0x7FFFD4, 0xB0E0E6, 0x5F9EA0, 0x4682B4, 0x6495ED, 0x00BFFF, 0x1E90FF, 0xADD8E6, 0x87CEEB, 0x87CEFA, 0x191970, 0x000080, 0x00008B, 0x0000CD, 0x0000FF, 0x4169E1, 0x8A2BE2, 0x4B0082, 0x483D8B, 0x6A5ACD, 0x7B68EE, 0x9370DB, 0x8B008B, 0x9400D3, 0x9932CC, 0xBA55D3, 0x800080, 0xD8BFD8, 0xDDA0DD, 0xEE82EE, 0xFF00FF, 0xDA70D6, 0xC71585, 0xDB7093, 0xFF1493, 0xFF69B4, 0xFFB6C1, 0xFFC0CB])

(def avail-ids (ref (shuffle ids))) 

(import '(java.util HashMap))

(defn make-item-builder
  "Create an item."
  [avail-ids]
  (fn []
    (dosync 
      (let [id (peek @avail-ids)]
        (alter avail-ids pop)
        (new HashMap {
         :id id
         :length (* 50 (inc (rand-int 7)))  
         :width (* 50 (inc (rand-int 7)))})))))

(def item-builder (make-item-builder avail-ids))

(def a-map (new HashMap {:y 1 :n 2}))
(:y a-map)

(item-builder)

(defn echo [say]
  (println say))