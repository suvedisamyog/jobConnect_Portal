<?php

require_once "../Data_operations/Job_Operation.php";
$response = array(); 

if($_SERVER["REQUEST_METHOD"]=="POST"){
    $jobId=$_POST["jID"];
    $title=$_POST["title"];
    $description=$_POST["description"];
    $empType=$_POST["empType"];
    $education=$_POST["education"];
    $experience=$_POST["experience"];
    $industry=$_POST["industry"];
    $category=$_POST["category"];
    $vacancies=$_POST["vacancies"];
    $salary=$_POST["salary"];
    if(isset($jobId)){
        $db=new Job();
        if($db->update_job($jobId,$title,$description,$empType,$education,$experience,$industry,$category,$vacancies,$salary)){
            $response['error'] = false; 
            $response['message'] = "Update Successfully"; 
        }else{
            $response['error'] = true; 
            $response['message'] = "Please try again, Error occured";  
        }

    }else{
        $response['error'] = true; 
        $response['message'] = "Id not found"; 
    }

}else{
    $response['error'] = true; 
    $response['message'] = "Invalid Request Method";
}
echo json_encode($response);