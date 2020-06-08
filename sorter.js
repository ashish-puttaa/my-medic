$('th').click(function() {

  var _this = $(this);
  var symbol_up_code = 8679;
  var symbol_down_code = 8681;
  var symbol_up = String.fromCharCode(symbol_up_code);
  var symbol_down = String.fromCharCode(symbol_down_code);

  // console.log(symbol_up + symbol_down);

  $(this).html(function() {

    var text = $(this).html();
    // console.log("Symbol " + text.charAt(text.length - 1));
    // console.log("Code " + symbol_up);
    // console.log("Check " + (text.charAt(text.length - 1) === symbol_up));
    if (text.charAt(text.length - 1) == symbol_up) {
      text = text.substring(0, text.length - 1);
      // console.log("Text", text);
      return text + symbol_down;
    } else if (text.charAt(text.length - 1) == symbol_down) {
      text = text.substring(0, text.length - 1);
      return text + symbol_up;
    } else {
      if (!this.asc) // Ascending
        return text + symbol_up;
      else(this.asc) // Descending
      return text + symbol_down;
    }
  });

  var table = $(this).parents('table').eq(0);
  var headrow = table.find('tr').eq(0).toArray();
  $(headrow).find("th").each(function() {
    // console.log(!$(this).is(_this));
    if (!$(this).is(_this)) { // Checks if the current "th" element is not the clicked "th"
      var content = $(this).text();
      // console.log(content.charAt(content.length - 2));
      // console.log(content.charAt(content.length - 2) == "^" || content.charAt(content.length - 2) == "v");
      if (content.charAt(content.length - 1) == symbol_up || content.charAt(content.length - 1) == symbol_down) {
        content = content.substring(0, content.length - 1);
        $(this).html(content);
      }
    }
  });

  var rows = table.find('tr:gt(0)').toArray().sort(compareFunction($(this).index()));
  // console.log("Asc" + this.asc);
  this.asc = !this.asc;
  console.log(this.asc);
  if (!this.asc) {
    rows = rows.reverse()
  }
  for (var i = 0; i < rows.length; i++) {
    table.append(rows[i])
  }
});

function compareFunction(index) {
  return function(a, b) {
    var valA = getCellValue(a, index);
    // console.log("valA = " + valA);
    var valB = getCellValue(b, index);
    // console.log("valB = " + valB);
    return $.isNumeric(valA) && $.isNumeric(valB) ? valA - valB : valA.toString().localeCompare(valB)
  }
}

function getCellValue(row, index) {
  return $(row).children('td').eq(index).text();
}