<?php
require_once '../Data_operations/Job_Operation.php';
$response = array();

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (isset($_POST['category_names'])) {
        $categoryNamesJsonString  = $_POST['category_names'];
        $categoryNamesArray  = json_decode($categoryNamesJsonString, true);

        $allJobsData = array();

        foreach ($categoryNamesArray as $categoryName) {
            $job = new Job();
            $results = $job->suggestedJobs($categoryName);
            if ($results) {
                $allJobsData[] = $results;
            } else {
                $response['error'] = false;
                $response['message'] = "No data found";
            }
        }
        // $job = new Job();
        // $allJobs = $job->allJobs();
        // if ($allJobs) {
        //     $allJobsData[] = $allJobs;
        // }
        $response['error'] = false;
        $response['data'] = $allJobsData;
        
    } else {
        $response['error'] = true;
        $response['message'] = "No categories received";
    }
} else {
    $response['error'] = true;
    $response['message'] = "Invalid Request Method";
}

echo json_encode($response);
