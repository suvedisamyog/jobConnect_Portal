<?php

require_once '../Data_operations/Job_Operation.php';
$response = array(); 

if ($_SERVER['REQUEST_METHOD'] === 'POST'){
    $email=$_POST['email'];
    if(isset($email)){
        $job = new Job();
        $results=$job->getAllAppliedJobs($email);
        if($results){
            $response['error'] = false;
            $response['data'] = $results;
        }else{
            $response['error'] = true;
            $response['message'] = "No data found";
        }
    }else{
        $response['error'] = true; 
		$response['message'] = "Email missing";
    }

   



}else{
    $response['error'] = true; 
    $response['message'] = "Invalid Request Method";
}
echo json_encode($response);