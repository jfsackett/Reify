package controller;

import model.PalletizerModel;
import view.PalletizerView;

public class PalletizerController {

	private PalletizerModel model;
	
	private PalletizerView view;

	public PalletizerController(PalletizerModel model, PalletizerView view) {
		this.model = model;
		this.view = view;
	}
	
	
}
