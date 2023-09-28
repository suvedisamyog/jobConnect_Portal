
<?php 

require_once '../Data_operations/Registartion_Login.php';
require_once '../Validations/CheckRegistrationData.php';

$response = array(); 

if($_SERVER['REQUEST_METHOD']=='POST'){
	$name=$_POST['name'];
	$email=$_POST['email'];	
	$userType=$_POST['userType'];
	$isComplete=$_POST['completeProfile'];
	if(
		isset($name) and 
			isset($email) and 
				isset($userType) and
				isset($isComplete)
				)
		{
		$check=new CheckRegistrationData();	
		$validationErrors = $check->validate($name, $email,);

		if(!empty($validationErrors)){
			$response['error'] = true;
            $response['message'] =  $validationErrors;
		}else{
			$db = new Registration_Login(); 

			$result = $db->createUser( $name,
										$email,
										$userType,
										$isComplete
										
									);
	
									if($result == 1){
										$response['error'] = false; 
										$response['message'] = "Registered successfully";
									}elseif($result == 2){
										$response['error'] = true; 
										$response['message'] = "Some error occurred please try again";			
									}elseif($result == 0){
										$response['error'] = true; 
										$response['message'] = "Email Already Registered	";						
									}
		}

	

	}else{
		$response['error'] = true; 
		$response['message'] = "Required fields are missing";
	}
}else{
	$response['error'] = true; 
	$response['message'] = "Invalid Request";
}



echo json_encode($response);