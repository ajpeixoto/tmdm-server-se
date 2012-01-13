Ext.namespace('amalto.updatereport');
amalto.updatereport.HistoryViewer = function(config) {

	Ext.applyIf(this, config);
	this.initUIComponents();
	amalto.updatereport.HistoryViewer.superclass.constructor.call(this);

};

var panelId = "datachangesviewer";

var JOURNAL_NAME = {
	'fr' : 'Journal->',
	'en' : 'Journal->'
			
};

Ext.extend(amalto.updatereport.HistoryViewer, Ext.Panel, {
	initUIComponents : function() {
	    Ext.apply(this, {
			layout : 'border',
			title : "Data Changes Viewer",
			id : panelId,
			closable:true,
			border : true,
			defaults: {			    
			    split: true,
			    bodyStyle: 'padding:15px'
			},
			items:[{
                    title:'Update report details',
                    region: 'north',
                    cmargins: '0 5 0 0',
                    height: 200,
                    cellCls: 'history-viewer-header',
                    animate : "false",
                    loader : new Ext.tree.TreeLoader({
                        dataUrl : "/updatereport/secure/updateReportDetails?ids="+this.ids
                    }),
                    xtype : "treepanel",
                    root : new Ext.tree.AsyncTreeNode({
                        expandable : true,
                        expanded : true,
                        text : "Update",
                        draggable : false,
                        id : "0"
                    }),                    
                    autoScroll : "true",
                    containerScroll : "true"
                }, {
                    xtype: "talend.documenthistorypanel",
                    cellCls: 'history-viewer-changes',
                    id:1,
                    parentPanelId:panelId,
                    title:'Before',
                    region: 'west',
                    width: '50%',
                    margins: '5 0 0 0',
                    cmargins: '5 5 0 0',
                    date:this.date,
                    key:this.key,
                    concept:this.concept,
                    dataCluster:this.dataCluster,
                    dataModel:this.dataModel,
                    action:'before',
                    autoScroll : "true"                    
                }, {
                    xtype: "talend.documenthistorypanel",
                    cellCls: 'history-viewer-changes',
                    id:2,
                    region: 'center',
                    width: '50%',
                    margins: '5 0 0 0',
                    parentPanelId:panelId,
                    title:'After',
                    date:this.date,
                    key:this.key,
                    concept:this.concept,
                    dataCluster:this.dataCluster,
                    dataModel:this.dataModel,
                    action:'current',
                    autoScroll : "true"                    
                }
            ],
            tbar: ['->',{
		        text : "Open Record",
		        iconCls:'report_bt_openRecord',
		        handler : function() {
		        	DWREngine.setAsync(false);
		        	var result = true;

		        	UpdateReportInterface.checkDCAndDM(this.dataCluster, this.dataModel, function(data) {
		        		result = data
		        	});

		        	if(result) {
		        		amalto.itemsbrowser.ItemsBrowser.editItemDetails(JOURNAL_NAME[language], this.key.split('\.'), this.concept, function(){});
		        	}
		        	else {
		        		Ext.MessageBox.alert("Error", "Please select the corresponding Data Container and Data Model.");
		        	}
		        	DWREngine.setAsync(true);
		        	}.createDelegate(this)
		        }]
		});
	}
});
