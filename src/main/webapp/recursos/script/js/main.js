function change_input_pass(inpt){
	if( $(inpt).attr('type')=="password" ){
		$(inpt).attr('type', "text");
	}else{
		$(inpt).attr('type', "password");
	}
}