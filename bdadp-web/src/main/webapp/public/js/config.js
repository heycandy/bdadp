require.config({
 // baseUrl: "/public/",
  baseUrl: window.document.location.pathname.substr(0,window.document.location.pathname.lastIndexOf("/")) + "/",
  paths: {
    /* 'jquery': "libs/plugins/jQuery/jquery.min.js"
     , html5shiv: 'libs/html5shiv/html5shiv.min'
     , respond: 'libs/respond/respond.min'
     , bootstrap: 'libs/bootstrap-3.3.6/js/bootstrap.min'
     , 'bootstrap-dialog': 'libs/bootstrap3-dialog/js/bootstrap-dialog.min'
     , 'bootstrapvalidator': "libs/bootstrapvalidator/js/bootstrapValidator.min"
     , 'bootstrap-wizard': 'libs/bootstrap-wizard/js/jquery.bootstrap.wizard'
     , 'international': 'js/international'
     , 'i18n': 'libs/jquery.i18n.properties'*/
  },
  shim: {
    /*   jquery: {
     deps: []
     }
     , bootstrap: {
     deps: ['jquery', 'css!bootstrap/../../css/bootstrap.min.css']
     }
     , 'bootstrap-dialog': {
     deps: ['jquery', 'bootstrap', 'css!bootstrap-dialog/../../css/bootstrap-dialog.min.css']
     }
     , 'bootstrapvalidator': {
     deps: ['jquery', 'bootstrap', 'css!bootstrapvalidator/../../css/bootstrapValidator.min.css']
     }
     , 'international': {
     deps: ['jquery', 'i18n']
     }
     , 'i18n': {
     deps: ['jquery']
     }*/
  }
});
