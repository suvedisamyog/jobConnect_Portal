<?php

require_once '../Data_operations/Registartion_Login.php';
$response = array(); 

if($_SERVER['REQUEST_METHOD']=='POST'){
    $email=$_POST['email'];
    $currentPassword=$_POST['currentPassword'];
    $newPassword=$_POST['newPassword'];
    $rePassword=$_POST['rePassword'];
    if(isset($email) and isset($currentPassword) && isset($rePassword) && ($newPassword)){
        $db=new Registration_Login();
        $result=$db->changePassword($email,$currentPassword,$newPassword,$rePassword);

        if($result == 0){
            $response['error'] = true; 
			$response['message'] = "New Password and Re-Password doesnot match";
        }else if($result==1){
            $response['error'] = false; 
			$response['message'] = "Password Updated";
        }else if($result==2){
            $response['error'] = true; 
			$response['message'] = "Error while updating";
        } else if($result==3){
            $response['error'] = true; 
			$response['message'] = "Incorrect! Current Password";
        }
    }else{
        $response['error'] = true; 
		$response['message'] = "Required fields are missing";    }
}else{
    $response['error'] = true; 
    $response['message'] = "Invalid Request Method";
}
echo json_encode($response);