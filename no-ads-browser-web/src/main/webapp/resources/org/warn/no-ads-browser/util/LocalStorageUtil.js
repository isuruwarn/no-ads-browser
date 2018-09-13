sap.ui.define([
	"sap/ui/base/Object",
	"sap/ui/base/ManagedObject"
], function( Object, ManagedObject ) {
	
	"use strict";
	
	return ManagedObject.extend("org.warn.no-ads-browser.util.LocalStorageUtil", {
		
		// for managed objects, the init function gets called by the UI5 framework 
		init: function () {
			this.oStorage = jQuery.sap.storage( jQuery.sap.storage.Type.local );
		},
		
		readFromStorage: function( key ) {
			return JSON.parse( this.oStorage.get( key ) );
		},
		
		updateStorage: function( key, obj ) {
			this.oStorage.put( key, JSON.stringify( obj ) );
		},
		
		deleteFromStorage: function( key ) {
			this.oStorage.remove( key );
		}
	
	});	
});