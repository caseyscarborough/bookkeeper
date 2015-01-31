// This is a manifest file that'll be compiled into application.js.
//
// Any JavaScript file within this directory can be referenced here using a relative path.
//
// You're free to add application-wide JavaScript to this file, but it's generally better 
// to create separate JavaScript files as needed.
//
//= require jquery/dist/jquery.min
//= require bootstrap/dist/js/bootstrap.min
//= require flat-ui/dist/js/flat-ui.min
//= require devbridge-autocomplete/dist/jquery.autocomplete.min
//= require bootstrap-datepicker/js/bootstrap-datepicker
//= require highcharts/highcharts
//= require highcharts/modules/drilldown
//= require spin.js/spin.js
//= require_tree .
//= require_self

if (typeof jQuery !== 'undefined') {
	(function($) {
		$('#spinner').ajaxStart(function() {
			$(this).fadeIn();
		}).ajaxStop(function() {
			$(this).fadeOut();
		});
	})(jQuery);
}
