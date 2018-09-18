sap.ui.define([
	"sap/ui/base/ManagedObject",
	'sap/ui/model/json/JSONModel',
	'org/warn/no-ads-browser/util/LocalStorageUtil'
], function( ManagedObject, JSONModel, LocalStorageUtil ) {
	
	"use strict";
	
	return ManagedObject.extend("org.warn.no-ads-browser.model.SettingsManager", {
		
		// for managed objects, the init function gets called by the UI5 framework
		init: function () {
			this.settingsKey = "nbSettings";
			this.storageUtil = new LocalStorageUtil();
		},
		
		getSettings: function() {
			var settings = this.storageUtil.readFromStorage( this.settingsKey );
			if( settings ) {
				return settings;
			} else {
				settings = {
						htmlView: true,
						plainTextView: false,
						cssDisabled: true,
						imagesDisabled: false,
						removeMetaTags: true,
					};
				this.storageUtil.updateStorage( this.settingsKey, settings );
				return settings;
			}
		},
		
		openSettingsMenu: function( oEvent, controller ) {
			var oButton = oEvent.getSource();
			// create menu only once
			if (!this.settingsMenu) {
				this.settingsMenu = sap.ui.xmlfragment( "org.warn.no-ads-browser.view.Settings", controller );
				controller.getView().addDependent(this.settingsMenu);
			}
			var eDock = sap.ui.core.Popup.Dock;
			var settings = this.getSettings();
			var settingsModel = new JSONModel();
			settingsModel.setData( settings );
			this.settingsMenu.setModel( settingsModel );
			this.settingsMenu.open( this._bKeyboard, oButton, eDock.BeginTop, eDock.BeginBottom, oButton );
		},
		
		toggleSelectSettings: function( key ) {
			var settings = this.storageUtil.readFromStorage( this.settingsKey );
			if( settings[key] ) {
				settings[key] = false;
			} else {
				settings[key] = true;
			}
			this.storageUtil.updateStorage( this.settingsKey, settings );
		}
	
	});	
});