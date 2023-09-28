<?php


require_once '../Data_operations/User_Operation.php';
$response = array(); 
if($_SERVER['REQUEST_METHOD']=='POST'){
    $email=$_POST['email'];
    if(isset($email)){
        $db=new User();
        $results = $db->fetchUserData($email);
        if($results){
            $response['error'] = false;
            $response['data'] = $results;
        }else{
            $response['error'] = true;
            $response['message'] = "No data found";
        }

    }else{
        $response['error'] = true; 
		$response['message'] = "Required fields are missing";
    }


}else{
    $response['error'] = true; 
    $response['message'] = "Invalid Request Method";
}
echo json_encode($response);
