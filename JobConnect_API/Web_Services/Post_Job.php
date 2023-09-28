<?php

require_once '../Data_operations/Job_Operation.php';
$response = array(); 


if ($_SERVER['REQUEST_METHOD'] === 'POST') {

    $title = trim($_POST['title']);
    $description = trim($_POST['description']);
    $empType = trim($_POST['empType']);
    $education = trim($_POST['education']);
    $experience = trim($_POST['experience']);
    $industry = trim($_POST['industry']);
    $category = trim($_POST['category']);
    $name = trim($_POST['name']);
    $email = trim($_POST['email']);
    $deadline = trim($_POST['deadline']);
    $salary = trim($_POST['salary']);
    $vacancies=trim($_POST["vacancies"]);



    if (!empty($title) && !empty($description) && !empty($empType) && !empty($education) && !empty($experience) && !empty($industry) && !empty($category) && !empty($name) && !empty($email) && !empty($deadline) && !empty($salary) && !empty($vacancies)){
        if($vacancies==0){
                $vacancies="Undefined";
        }
        if($empType=="Select One"){
            $empType=="Not Mentioned";
        }


        $currentTimestamp = date('Y-m-d H:i:s'); 

        $daysMap = [
            "After 3 Days" => 3,
            "After 5 Days" => 5,
            "After 7 Days" => 7,
            "After 9 Days" => 9,
            "After 13 Days" => 13
        ];

        if($education=="Select One" || $industry=="" || $category==""){
            $response['error'] = true;
            $response['message'] = "Select One is not valid";
        }else{
            $daysToAdd = isset($daysMap[$deadline]) ? $daysMap[$deadline] : 3;
            $deadlineTimestamp = date('Y-m-d H:i:s', strtotime("+$daysToAdd days", strtotime($currentTimestamp)));
            $job = new Job();
            $result=$job->postjob($title, $description, $empType, $education, $experience, $industry, $category, $name, $email,$deadlineTimestamp,$salary,$vacancies);
    
            if($result==1){
                $response['error'] = false;
                $response['message'] = "Job Posted successfully";
            }else if($result==2){
                $response['error'] = true;
                $response['message'] = "Failed to Post Job,Try Again";
            }
        }        

}else{
    $response['error'] = true; 
    $response['message'] = "One or more Fields Are Empty,Try Again";
   }
}

else{
    $response['error'] = true; 
    $response['message'] = "Invalid Request Method";
}
echo json_encode($response);
