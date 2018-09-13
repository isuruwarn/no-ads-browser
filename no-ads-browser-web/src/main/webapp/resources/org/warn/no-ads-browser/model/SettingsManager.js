sap.ui.define([
	"sap/ui/base/ManagedObject",
	'org/warn/no-ads-browser/util/LocalStorageUtil'
], function( ManagedObject, LocalStorageUtil ) {
	
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
						cssDisabled: false,
						imagesDisabled: false,
						removeMetaTags: false,
					};
				this.storageUtil.updateStorage( this.settingsKey, settings );
				return settings;
			}
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