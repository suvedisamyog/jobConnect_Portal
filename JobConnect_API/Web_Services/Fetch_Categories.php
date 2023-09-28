<?php
require_once '../Data_operations/Job_Operation.php';
$response = array(); 


if($_SERVER['REQUEST_METHOD']=='POST'){
    $email=$_POST['email'];
    if(isset($email)){
        $db=new Job();
        $result1 = $db->fetchUserInterest($email);
        $result2 = $db->fetchUserApplied($email);
        
        if($result1 ){
            $response['error'] = false;
            $response['InterestCate'] = $result1;
            $response['AppliedCat'] = $result2;
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