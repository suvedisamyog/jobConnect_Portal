<?php

require_once '../Data_operations/Job_Operation.php';
$response = array(); 


if($_SERVER["REQUEST_METHOD"]=="POST"){
    $jobId=$_POST["j_id"];
    if(isset($jobId)){
        $db=new Job;
        if($db->deleteJob($jobId)){
            $response['error'] = false;
            $response['message'] = "Deleted successfully";
        }else{
            $response['error'] = true;
            $response['message'] = "Error while Deleting";
        }

    }else{
        $response['error'] = true;
        $response['message'] = "Particular Id not found";
    }


}else{
    $response['error'] = true; 
    $response['message'] = "Invalid Request Method";
}
echo json_encode($response);