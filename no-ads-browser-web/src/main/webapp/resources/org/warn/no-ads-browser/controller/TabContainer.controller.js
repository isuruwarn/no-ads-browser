sap.ui.define([
	'sap/ui/core/mvc/Controller',
	'sap/ui/model/json/JSONModel',
	'sap/m/MessageToast',
	'sap/m/Button',
	'sap/m/Dialog',
	'sap/m/TabContainerItem',
	'sap/m/MessageBox'
	],
	function( Controller, JSONModel, MessageToast, Button, Dialog, TabContainerItem, MessageBox ) {
		
		"use strict";

		return Controller.extend("org.warn.no-ads-browser.TabContainer", {
			
			svcUrl: null,
			settingsKey: null,
			allUrlsKey: null,
			prevSessionUrlsKey: null,
			allUrls: null,
			prevSessionUrls: null,
			oSelectedTabItem: null,
			oResourceModel: null,
			oStorage: null,
			oSettings: null,
			
			onInit: function () {
				
				this.svcUrl = "/no-ads-browser-web/svc/rs";
				this.allUrlsKey = "nbAllUrls";
				this.prevSessionUrlsKey = "nbSessionUrls";
				this.settingsKey = "nbSettings";
				
				this.oResourceModel = this.getView().getModel("i18n").getResourceBundle();
				
				this.oStorage = jQuery.sap.storage( jQuery.sap.storage.Type.local );
				
				this.oSettings = this.getSettings();
				
				var allUrls = this.readFromStorage( this.allUrlsKey );
				this.allUrls = allUrls?allUrls:[];
				
				var prevSessionUrls = this.readFromStorage( this.prevSessionUrlsKey );
				this.prevSessionUrls = prevSessionUrls?prevSessionUrls:[];
				this.restorePreviousTabs();
			},
			
			onItemSelected: function(oEvent) {
				this.oSelectedTabItem = oEvent.getSource();
			},
			
			newTabHandler: function() {
				this.createNewTab( null );
			},
			
			go: function(oEvent) {
				
				var cntrlrObj = this;
				var url = this.oSelectedTabItem.getContent()[0].getContent()[0].getValue();
				
				if( url && url!="" ) {
					
					sap.ui.core.BusyIndicator.show();
					
					url = url.trim();
					var queryStr = "?nbturl=" + url + '&nbtxt=' + this.oSettings.plainTextView + '&nbcss=' + this.oSettings.cssDisabled 
						+ '&nbimgs=' + this.oSettings.imagesDisabled + '&nbmeta=' + this.oSettings.removeMetaTags;
					
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
								cntrlrObj.oSelectedTabItem.removeContent(1);
								cntrlrObj.oSelectedTabItem.insertContent( html, 1 );
								cntrlrObj.saveUrl( data.url );
							}
						},
						error : function(data){
							sap.ui.core.BusyIndicator.hide();
							
						}
	
					});
				}
			},
			
			settings: function(oEvent) {
				var oButton = oEvent.getSource();
				// create menu only once
				if (!this.settingsMenu) {
					this.settingsMenu = sap.ui.xmlfragment( "org.warn.no-ads-browser.view.MenuItemEventing", this );
					this.getView().addDependent(this.settingsMenu);
				}
				var eDock = sap.ui.core.Popup.Dock;
				var settingsModel = new JSONModel();
				settingsModel.setData( this.oSettings );
				this.settingsMenu.setModel( settingsModel );
				this.settingsMenu.open( this._bKeyboard, oButton, eDock.BeginTop, eDock.BeginBottom, oButton );
			},

			tabCloseHandler: function(oEvent) {
				var tabToClose = oEvent.getParameter('item');
				var url = tabToClose.getContent()[0].getContent()[0].getValue();
				var i = this.prevSessionUrls.indexOf( url );
				if( i != -1 ) {
					this.prevSessionUrls.splice( i, 1 );
					this.updateStorage( this.prevSessionUrlsKey, this.prevSessionUrls );
				}
			},
			
			restorePreviousTabs: function() {
				for( var i=0; i<this.prevSessionUrls.length; i++ ) {
					this.createNewTab( this.prevSessionUrls[i] );
				}
			},
			
			createNewTab: function( sUrl ) {
				var newTab = sap.ui.xmlfragment( "org.warn.no-ads-browser.view.TabContainerItem", this );
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
				var tabContainer = this.byId("browserTabContainer");
				tabContainer.addItem( newTab );
				tabContainer.setSelectedItem( newTab );
				this.oSelectedTabItem = newTab;
			},
			
			handleMenuItemPress: function(oEvent) {
				
				var selectedItem = oEvent.getParameter("item").getText();
				
				if( selectedItem == this.oResourceModel.getText("HTML_VIEW_MENU_TEXT") ) {
					this.toggleSelectSettings( "htmlView" );
					this.toggleSelectSettings( "plainTextView" );
					this.go(oEvent);
					
				} else if( selectedItem == this.oResourceModel.getText("TEXT_VIEW_MENU_TEXT") ) {
					this.toggleSelectSettings( "plainTextView" );
					this.toggleSelectSettings( "htmlView" );
					this.go(oEvent);
					
				} else if( selectedItem == this.oResourceModel.getText("VIEW_INPUT_MENU_TEXT") ) {
					var oModel = this.oSelectedTabItem.getModel();
					if( oModel ) {
						var inputSrc = oModel.getData().input;
						this.openDialog( this.oResourceModel.getText("VIEW_INPUT_DIALOG_TITLE"), inputSrc );
					}
					
				} else if( selectedItem == this.oResourceModel.getText("VIEW_OUTPUT_MENU_TEXT") ) {
					var oModel = this.oSelectedTabItem.getModel();
					if( oModel && this.oSettings.htmlView ) {
						var outputSrc = oModel.getData().output;
						this.openDialog( this.oResourceModel.getText("VIEW_OUTPUT_DIALOG_TITLE"), outputSrc );
					}
					
				} else if( selectedItem == this.oResourceModel.getText("DISABLE_CSS_MENU_TEXT") ) {
					this.toggleSelectSettings( "cssDisabled" );
					this.go(oEvent);
					
				} else if( selectedItem == this.oResourceModel.getText("DISABLE_IMGS_MENU_TEXT") ) {
					this.toggleSelectSettings( "imagesDisabled" );
					this.go(oEvent);
					
				} else if( selectedItem == this.oResourceModel.getText("REMOVE_META_TAGS_MENU_TEXT") ) {
					this.toggleSelectSettings( "removeMetaTags" );
					this.go(oEvent);
					
				} else if( selectedItem == this.oResourceModel.getText("CLEAR_CACHE_MENU_TEXT") ) {
					this.deleteFromStorage( this.allUrlsKey );
					this.deleteFromStorage( this.prevSessionUrlsKey );
					MessageToast.show( this.oResourceModel.getText("CACHE_CLEARED_MSG") );
				}
				
			},
			
			readFromStorage: function( key ) {
				return JSON.parse( this.oStorage.get( key ) );
			},
			
			getSettings: function() {
				var settings = this.readFromStorage( this.settingsKey );
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
					this.updateStorage( this.settingsKey, settings );
					return settings;
				}
			},
			
			updateStorage: function( key, obj ) {
				this.oStorage.put( key, JSON.stringify( obj ) );
			},
			
			deleteFromStorage: function( key ) {
				this.oStorage.remove( key );
			},
			
			toggleSelectSettings: function( settingsKey ) {
				if( this.oSettings[settingsKey] ) {
					this.oSettings[settingsKey] = false;
				} else {
					this.oSettings[settingsKey] = true;
				}
				this.updateStorage( this.settingsKey, this.oSettings );
			},
			
			saveUrl: function( url ) {
				if( this.allUrls.indexOf( url ) == -1 ) {
					if( !( this.allUrls.length < 100 ) ) { // if max capacity has been reached, remove oldest element
						this.allUrls.shift();
					}
					this.allUrls.push( url );
					this.updateStorage( this.allUrlsKey, this.allUrls );
				}
				if( this.prevSessionUrls.indexOf( url ) == -1 ) {
					this.prevSessionUrls.push( url );
					this.updateStorage( this.prevSessionUrlsKey, this.prevSessionUrls );
				}
			},
			
			openDialog: function( sTitle, sContent ) {
//				var cntrlrObj = this;
//				var dialog = new Dialog({
//					title: sTitle,
//					type: 'Message',
//					content: new Text({
//						text: sContent
//					}),
//					beginButton: new Button({
//						text: cntrlrObj.oResourceModel.getText("DIALOG_OK_BTN_TEXT"),
//						press: function () {
//							dialog.close();
//						}
//					}),
//					afterClose: function() {
//						dialog.destroy();
//					}
//				});
//				dialog.open();
				var popupWindow = window.open( "", sTitle, "width=600,height=700");
				popupWindow.document.write( "<textarea rows=\"44\" cols=\"80\" wrap='off' readonly>" + sContent + "</textarea>" ); 
			}
			
		});
	}
);
