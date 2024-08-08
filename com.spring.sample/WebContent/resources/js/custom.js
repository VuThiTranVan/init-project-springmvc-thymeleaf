$(document).ready(function() {
  var _csrf_token = $("meta[name='_csrf']").attr("content");
  var _csrf_parameter = $("meta[name='_csrf_parameter']").attr("content");
  var _csrf_header = $("meta[name='_csrf_header']").attr("content");
  $(document).ajaxSend(function(e, xhr, options) {
    xhr.setRequestHeader(_csrf_header, _csrf_token);
  });
  
  $('a').filter('[data-method="delete"], [data-method="DELETE"]').click(function(event) {
	event.preventDefault();
	event.stopPropagation();
	  
	url = $(this).attr('href');
	  
	var isAjax = $(this).data('remote');
	if(isAjax) {
      var id = $(this).data('id');
      $.ajax({
		type : 'DELETE',
		contentType : "application/json",
		url : $(this).attr('href'),
		dataType : 'json',
		success : function(data, textStatus, xhr) {
	      var response = JSON.parse(data);
		  if(response.result == 'OK') {
			  var id = response.id;
			  var model = response.model;
	        $('#' + model + '-' + id).remove();
		  }  
		}
      });
	} else {
      var form = document.createElement('form');
	  form.action = url;
	  form.method = 'POST';

	  var input = document.createElement('input');
	  input.type = 'hidden';
	  input.name = '_method';
	  input.value = 'delete';
	  document.body.appendChild(form);
	  form.appendChild(input);
	  
	  input = document.createElement('input');
	  input.type = 'hidden';
	  input.name = _csrf_parameter;
	  input.value = _csrf_token;
	  document.body.appendChild(form);
	  form.appendChild(input);
	  
	  form.submit();
	}
  });
  
});