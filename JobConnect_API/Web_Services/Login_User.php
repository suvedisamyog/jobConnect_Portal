<?php

require_once '../Data_operations/Registartion_Login.php';
$response = array(); 

if($_SERVER['REQUEST_METHOD']=='POST'){
    $email=$_POST['email'];
    if(isset($email)){
        $db=new Registration_Login();
        if($db->userLogin($email)){
            $user=$db->getUserDetail($email);
            $response['error'] = false; 
            $response['email'] = $user['Email'];
            $response['name'] = $user['Name'];
            $response['userType'] = $user['UserType'];
            $response['isComplete']=$user['isComplete'];
            $response['message'] = "Login Successfull";		
        }else{
            $response['error'] = true; 
			$response['message'] = "Invalid email ";
        }     
    }else{
        $response['error'] = true; 
		$response['message'] = "Required fields are missing";    }
}else{
    $response['error'] = true; 
    $response['message'] = "Invalid Request Method";
}
echo json_encode($response);