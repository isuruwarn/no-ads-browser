sap.ui.define([
	'sap/ui/core/mvc/Controller',
	'sap/ui/model/json/JSONModel',
	'sap/m/MessageToast',
	'sap/m/Button',
	'sap/m/Dialog',
	'org/warn/no-ads-browser/model/SettingsManager',
	'org/warn/no-ads-browser/model/TabManager'
	],
	function( Controller, JSONModel, MessageToast, Button, Dialog, SettingsManager, TabManager ) {
		
		"use strict";

		return Controller.extend("org.warn.no-ads-browser.Browser", {
			
			onInit: function() {
				this.svcUrl = "/no-ads-browser-web/svc/rs";
				this.oResourceModel = this.getView().getModel("i18n").getResourceBundle();
				this.settingsManager = new SettingsManager();
				this.tabManager = new TabManager();
				this.tabManager.restorePreviousTabs( this );
			},
			
			openNewTab: function() {
				this.tabManager.createNewTab( null, this );
			},
			
			selectTab: function(oEvent) {
				this.oSelectedTabItem = oEvent.getParameter('item');
			},
			
			closeTab: function(oEvent) {
				var tabToClose = oEvent.getParameter('item');
				this.tabManager.closeTab( tabToClose );
			},
			
			go: function(oEvent) {
				
				var cntrlrObj = this;
				var url = this.oSelectedTabItem.getContent()[0].getValue();
				
				if( url && url!="" ) {
					
					sap.ui.core.BusyIndicator.show();
					
					url = url.trim();
					var oSettings = this.settingsManager.getSettings();
					var queryStr = "?nbturl=" + url + '&nbtxt=' + oSettings.plainTextView + '&nbcss=' + oSettings.cssDisabled 
						+ '&nbimgs=' + oSettings.imagesDisabled + '&nbmeta=' + oSettings.removeMetaTags;
					
					$.ajax({
						url: this.svcUrl + queryStr,
						type: "GET",
						headers : {
							"X-Requested-With": "XMLHttpRequest",
							"Accept": "application/json"
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
									message: data.message,
									input: data.input,
									output: data.output
								});
								cntrlrObj.oSelectedTabItem.setModel(oModel);
								var content = "<div id=\"wrapper\" class=\"transparentBg\"><br><br>" + data.output + "</div>";
								//var content = "<iframe srcdoc=\"" + data.output + "\"></iframe>";
								var html = new sap.ui.core.HTML();
								html.setContent(content);
								cntrlrObj.oSelectedTabItem.removeContent(3);
								cntrlrObj.oSelectedTabItem.insertContent( html, 3 );
								cntrlrObj.tabManager.saveUrl( data.url );
							}
						},
						error : function(data){
							sap.ui.core.BusyIndicator.hide();
							
						}
	
					});
				}
			},
			
			openSettingsMenu: function(oEvent) {
				this.settingsManager.openSettingsMenu( oEvent, this );
			},
			
			selectMenuItem: function(oEvent) {
				
				var selectedItem = oEvent.getParameter("item").getText();
				
				if( selectedItem == this.oResourceModel.getText("HTML_VIEW_MENU_TEXT") ) {
					this.settingsManager.toggleSelectSettings( "htmlView" );
					this.settingsManager.toggleSelectSettings( "plainTextView" );
					this.go(oEvent);
					
				} else if( selectedItem == this.oResourceModel.getText("TEXT_VIEW_MENU_TEXT") ) {
					this.settingsManager.toggleSelectSettings( "plainTextView" );
					this.settingsManager.toggleSelectSettings( "htmlView" );
					this.go(oEvent);
					
				} else if( selectedItem == this.oResourceModel.getText("VIEW_INPUT_MENU_TEXT") ) {
					var oModel = this.oSelectedTabItem.getModel();
					if( oModel ) {
						var inputSrc = oModel.getData().input;
						this.openContentWindow( this.oResourceModel.getText("VIEW_INPUT_DIALOG_TITLE"), inputSrc );
					}
					
				} else if( selectedItem == this.oResourceModel.getText("VIEW_OUTPUT_MENU_TEXT") ) {
					var oModel = this.oSelectedTabItem.getModel();
					var oSettings = this.settingsManager.getSettings();
					if( oModel && oSettings.htmlView ) {
						var outputSrc = oModel.getData().output;
						this.openContentWindow( this.oResourceModel.getText("VIEW_OUTPUT_DIALOG_TITLE"), outputSrc );
					}
					
				} else if( selectedItem == this.oResourceModel.getText("DISABLE_CSS_MENU_TEXT") ) {
					this.settingsManager.toggleSelectSettings( "cssDisabled" );
					this.go(oEvent);
					
				} else if( selectedItem == this.oResourceModel.getText("DISABLE_IMGS_MENU_TEXT") ) {
					this.settingsManager.toggleSelectSettings( "imagesDisabled" );
					this.go(oEvent);
					
				} else if( selectedItem == this.oResourceModel.getText("REMOVE_META_TAGS_MENU_TEXT") ) {
					this.settingsManager.toggleSelectSettings( "removeMetaTags" );
					this.go(oEvent);
					
				} else if( selectedItem == this.oResourceModel.getText("CLEAR_CACHE_MENU_TEXT") ) {
					this.tabManager.discardUrls();
					MessageToast.show( this.oResourceModel.getText("CACHE_CLEARED_MSG") );
				}
				
			},
			
			openContentWindow: function( sTitle, sContent ) {
				var popupWindow = window.open( "", sTitle, "width=600,height=700");
				popupWindow.document.write( "<textarea rows=\"44\" cols=\"80\" wrap='off' readonly>" + sContent + "</textarea>" ); 
			}
			
		});
	}
);
