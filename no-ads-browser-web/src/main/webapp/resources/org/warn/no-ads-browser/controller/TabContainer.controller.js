sap.ui.define([
	'sap/ui/core/mvc/Controller', 
	'sap/ui/model/json/JSONModel', 
	'sap/m/MessageToast', 
	'sap/m/TabContainerItem', 
	'sap/m/MessageBox'
	],
	function( Controller, JSONModel, MessageToast, TabContainerItem, MessageBox ) {
		
		"use strict";

		return Controller.extend("org.warn.no-ads-browser.TabContainer", {
			
			svcUrl: null,
			selectedTabItem: null,
			
			onInit: function () {
				this.svcUrl = '/no-ads-browser-web/svc/rs';
				this.createNewTab();
			},
			
			onItemSelected: function(oEvent) {
				this.selectedTabItem = oEvent.getSource();
			},
			
			addNewButtonPressHandler : function() {
				this.createNewTab();
			},
			
			go: function(oEvent) {
				
				sap.ui.core.BusyIndicator.show();
				var cntrlrObj = this;
				var url = this.selectedTabItem.getContent()[0].getContent()[0].getValue();
				var queryStr = '?url=' + url;
				// call backend service	
				$.ajax({
					url: this.svcUrl + queryStr,
					type: "GET",
					headers : {
						//"X-Requested-With": "XMLHttpRequest",
						//"Content-Type": "application/json",
						//"Cache-Control" : "no-cache"
					},
					data : null,
					dataType : "json",
					async : false,
					success: function(data) {
						sap.ui.core.BusyIndicator.hide();
						if( data ) { 
							var oModel = new JSONModel();
							oModel.setData({
								url: data.url,
								title: data.title,
								message: data.message
							});
							cntrlrObj.selectedTabItem.setModel(oModel);
							
							//var outputHTML = '<html><head><title></title><head><body border=1><h1>Hello World</h1></body></html>';
							var content = '<div id="wrapper"><br><br>' + data.output + '</div>';
							var html = new sap.ui.core.HTML();
							html.setContent(content);
							cntrlrObj.selectedTabItem.removeContent(1);
							cntrlrObj.selectedTabItem.insertContent( html, 1 );
						}
					},
					error : function(data){
						sap.ui.core.BusyIndicator.hide();
						
					}

				});
				
			},
			
			settings: function() {
				
			},

			itemCloseHandler: function(oEvent) {
				
			},
			
			createNewTab: function() {
				var newTab = sap.ui.xmlfragment( "org.warn.no-ads-browser.view.TabContainerItem", this );
				//this.getView().addDependent(newTab);
				var tabContainer = this.byId("browserTabContainer");
				tabContainer.addItem( newTab );
				tabContainer.setSelectedItem( newTab );
				this.selectedTabItem = newTab;
			},
			
			populateTab: function( newTab ) {
				
			}
			
		});
	}
);
