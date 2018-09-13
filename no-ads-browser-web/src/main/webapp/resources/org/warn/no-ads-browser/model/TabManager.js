sap.ui.define([
	"sap/ui/base/ManagedObject",
	'sap/ui/model/json/JSONModel',
	'org/warn/no-ads-browser/util/LocalStorageUtil'
], function( ManagedObject, JSONModel, LocalStorageUtil ) {
	
	"use strict";
	
	return ManagedObject.extend("org.warn.no-ads-browser.model.TabManager", {
		
		// for managed objects, the init function gets called by the UI5 framework
		init: function () {
			this.allUrlsKey = "nbAllUrls";
			this.prevSessionUrlsKey = "nbSessionUrls";
			
			this.storageUtil = new LocalStorageUtil();
			
			var allUrls = this.storageUtil.readFromStorage( this.allUrlsKey );
			this.allUrls = allUrls?allUrls:[];
			
			var prevSessionUrls = this.storageUtil.readFromStorage( this.prevSessionUrlsKey );
			this.prevSessionUrls = prevSessionUrls?prevSessionUrls:[];
		},
		
		createNewTab: function( sUrl, controller ) {
			var newTab = sap.ui.xmlfragment( "org.warn.no-ads-browser.view.TabContainerItem", controller );
			var sTitle = sUrl;
			if( !sUrl ) {
				sTitle = "New Tab";
			}
			var oModel = new JSONModel();
			oModel.setData({
				url: sUrl,
				title: sTitle
			});
			newTab.setModel(oModel);
			var tabContainer = controller.byId("browserTabContainer");
			tabContainer.addItem( newTab );
			tabContainer.setSelectedItem( newTab );
		},
		
		restorePreviousTabs: function( controller ) {
			for( var i=0; i<this.prevSessionUrls.length; i++ ) {
				this.createNewTab( this.prevSessionUrls[i], controller );
			}
		},
		
		closeTab: function( tabToClose ) {
			var url = tabToClose.getContent()[0].getValue();
			var i = this.prevSessionUrls.indexOf( url );
			if( i != -1 ) {
				this.prevSessionUrls.splice( i, 1 );
				this.storageUtil.updateStorage( this.prevSessionUrlsKey, this.prevSessionUrls );
			}
		},
		
		// TODO move to better location?
		saveUrl: function( url ) {
			if( this.allUrls.indexOf( url ) == -1 ) {
				if( !( this.allUrls.length < 100 ) ) { // if max capacity has been reached, remove oldest element
					this.allUrls.shift();
				}
				this.allUrls.push( url );
				this.storageUtil.updateStorage( this.allUrlsKey, this.allUrls );
			}
			if( this.prevSessionUrls.indexOf( url ) == -1 ) {
				this.prevSessionUrls.push( url );
				this.storageUtil.updateStorage( this.prevSessionUrlsKey, this.prevSessionUrls );
			}
		},
		
		// TODO move to better location?
		discardUrls: function() {
			this.storageUtil.deleteFromStorage( this.allUrlsKey );
			this.storageUtil.deleteFromStorage( this.prevSessionUrlsKey );
		}
	
	});	
});