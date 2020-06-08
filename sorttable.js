/**
 * mini jQuery plugin based on the answer by Nick Grealy
 *
 * https://stackoverflow.com/a/19947532/4241030
 */
(function($) {
  $.fn.extend({
    makeSortable: function() {
      var MyTable = this;

      var getCellValue = function(row, index) {
        return $(row).children('td').eq(index).text();
      };

      MyTable.find('th').click(function() {
        var table = $(this).parents('table').eq(0);

        // Sort by the given filter
        var rows = table.find('tr:gt(0)').toArray().sort(function(a, b) {
          var index = $(this).index();
          var valA = getCellValue(a, index),
            valB = getCellValue(b, index);

          return $.isNumeric(valA) && $.isNumeric(valB) ? valA - valB : valA.localeCompare(valB);
        });

        this.asc = !this.asc;

        if (!this.asc) {
          rows = rows.reverse();
        }

        for (var i = 0; i < rows.length; i++) {
          table.append(rows[i]);
        }
      });
    }
  });
})(jQuery);


$(function() {
  $("#table").makeSortable();
});