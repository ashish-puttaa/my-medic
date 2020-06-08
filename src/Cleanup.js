//  sortTable(f,n)
//  f : 1 ascending order, -1 descending order
//  n : n-th child(<td>) of <tr>
function sortTable(f, n) {
  var rows = $('#mytable tbody  tr').get();

  rows.sort(function(a, b) {

    var A = getVal(a);
    var B = getVal(b);

    if (A < B) {
      return -1 * f;
    }
    if (A > B) {
      return 1 * f;
    }
    return 0;
  });

  function getVal(elm) {
    var v = $(elm).children('td').eq(n).text().toUpperCase();
    if ($.isNumeric(v)) {
      v = parseInt(v, 10);
    }
    return v;
  }

  $.each(rows, function(index, row) {
    $('#mytable').children('tbody').append(row);
  });
}



var f_sl = 1; // flag to toggle the sorting order
var f_nm = 1; // flag to toggle the sorting order
$("#sl").click(function() {
  f_sl *= -1; // toggle the sorting order
  var n = $(this).prevAll().length;
  sortTable(f_sl, n);
});
$("#nm").click(function() {
  f_nm *= -1; // toggle the sorting order
  var n = $(this).prevAll().length;
  sortTable(f_nm, n);
});