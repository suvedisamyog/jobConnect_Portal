<?php

class Job{

    private $con;
    private  $currentDateTime;


    function __construct()
    {
        require_once '../includes/DbConnection.php';
        $db = new DbConnect();
        $this->con = $db->connect(); 
        $this->currentDateTime = date('Y-m-d H:i:s');
        $this->checkJobDeadline();
  
    }
    public function checkJobDeadline(){    
        $stmt = $this->con->prepare("SELECT j_id,deadline FROM jobs ");
        $stmt->execute();
        $result = $stmt->get_result();
    
        while ($row = $result->fetch_assoc()) {
          $deadline=$row['deadline'];
          $newDeadline = date('Y-m-d H:i:s', strtotime($deadline . ' +2 months'));
          if( $this->currentDateTime>$newDeadline){
            $this->deleteJob($row['j_id']);

          }

        }
    }
    

public function postjob($title, $description, $empType, $education, $experience, $industry, $category, $name, $email,$deadline,$salary,$vacancies){
  $stmt = $this->con->prepare("INSERT INTO jobs (j_title, j_description, j_empType, j_education, j_experience, j_industry, j_category,j_name, j_email,deadline,j_salary,vacancies) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)");
  $stmt->bind_param("ssssssssssss", $title, $description, $empType, $education, $experience, $industry, $category, $name, $email,$deadline,$salary,$vacancies);
  if ($stmt->execute()) {
    return 1;
} else {
    return 2; 
}

        
    }

  public function JobsByOrg($email){
    $stmt=$this->con->prepare("SELECT * FROM jobs WHERE j_email=? ORDER BY j_id DESC");

    $stmt->bind_param("s",$email);
    $stmt->execute();
    $result = $stmt->get_result();
    $jobs = array();
     $this->currentDateTime = date('Y-m-d H:i:s');

    while ($row = $result->fetch_assoc()) {
      if ($row['deadline'] <  $this->currentDateTime) {
        $this->deleteJob($row['j_id']);
      
    } else {
        $jobs[] = $row;
    }
  }
  return $jobs;

  }  

  public function deleteJob($jobId) {
        $stmt1 = $this->con->prepare("DELETE FROM jobs WHERE j_id = ?");
    $stmt1->bind_param("i", $jobId);
    $stmt1->execute();
    
    $stmt2 = $this->con->prepare("DELETE FROM applied_jobs WHERE Job_id = ?");
    $stmt2->bind_param("i", $jobId);
    $stmt2->execute();
    
    $stmt3 = $this->con->prepare("DELETE FROM job_saved WHERE job_id = ?");
    $stmt3->bind_param("i", $jobId);
    $stmt3->execute();

  
    
    return ($stmt1->affected_rows > 0 || $stmt2->affected_rows > 0 || $stmt3->affected_rows > 0);
}


public function update_job($jobId,$title,$description,$empType,$education,$experience,$industry,$category,$vacancies,$salary){
      $stmt = $this->con->prepare("UPDATE jobs SET j_title = ?, j_description = ?, j_empType = ?, j_education = ?, j_experience = ?, j_industry = ?, j_category = ? ,j_salary=?,vacancies=? WHERE j_id = ?");
     
      $stmt->bind_param("ssssssssss", $title, $description, $empType, $education, $experience, $industry, $category,$salary,$vacancies,$jobId);
      if ($stmt->execute()) {
        return true;
    } else {
        return false;
    }

}

public function allJobs(){
  $stmt=$this->con->prepare("SELECT * FROM jobs  ORDER BY j_id DESC");
  $stmt->execute();
  $result = $stmt->get_result();
  $jobs = array();
  while ($row = $result->fetch_assoc()){
    if ($row['deadline'] >  $this->currentDateTime) {
      $jobs[] = $row;
  }  }
  return $jobs;

}

public function suggestedJobs($categoryName){
  $stmt=$this->con->prepare("SELECT * FROM jobs WHERE j_category=? ORDER BY j_id DESC");
  $stmt->bind_param("s", $categoryName);
  $stmt->execute();
  $result = $stmt->get_result();
  if ($result->num_rows > 0) {
   $jobs = array();
  while ($row = $result->fetch_assoc()){
    if ($row['deadline'] >  $this->currentDateTime) {
      $jobs[] = $row;
  }  }
  return $jobs;
} else {
  
    return false;
}
  
} 



public function getAllSavedJobs($email){
  $stmt=$this->con->prepare("SELECT * FROM job_saved WHERE user_email=? ORDER BY saved_id  DESC");
  $stmt->bind_param("s",$email);
  $stmt->execute();
  $result = $stmt->get_result();
  $jobs = array();
  while ($row = $result->fetch_assoc()){
    $jobId = $row['job_id'];
    $jobData = $this->getJobData($jobId);

    if ($jobData && $jobData['deadline'] >  $this->currentDateTime) {
      $jobs[] = $jobData;
  }
}
  return $jobs;
}

public function getJobData($jobId){
  $stmt = $this->con->prepare("SELECT * FROM jobs WHERE j_id=?");
  $stmt->bind_param("s", $jobId);
  $stmt->execute();
  $result = $stmt->get_result();
  
  if($row = $result->fetch_assoc()){
    return $row;
  }
  

}

public function toggleJob($jobId, $userEmail) {
  $stmt = $this->con->prepare("SELECT * FROM job_saved WHERE job_id = ? AND user_email = ?");
  $stmt->bind_param("is", $jobId, $userEmail);
  $stmt->execute();
  $result = $stmt->get_result();
 if($result->num_rows > 0){
  $stmt = $this->con->prepare("DELETE FROM job_saved WHERE job_id = ? AND user_email = ?");
  $stmt->bind_param("is", $jobId, $userEmail);
  $stmt->execute();
  if ($stmt->affected_rows > 0){
    return "Removed";
  }else{
    return "Error while Removing";
  } 
 }else{
  $stmt = $this->con->prepare("INSERT INTO  job_saved (job_id,user_email) VALUES (?,?)");
  $stmt->bind_param("is", $jobId, $userEmail);
  if ($stmt->execute()) {
    return "Added";
} else {
    return "Error while adding"; 
} 
}
}

public function SavedJobs($userEmail){
  $stmt=$this->con->prepare("SELECT job_id FROM job_saved WHERE user_email=? ORDER BY saved_id  DESC");
  $stmt->bind_param("s",$userEmail);
  $stmt->execute();
  $result = $stmt->get_result();
  $jobId = array();
  while ($row = $result->fetch_assoc()){
    $jobId[] = $row;
  }
  return $jobId;

  
} 

  public function checkId($Email,$jId){
    $stmt=$this->con->prepare("SELECT * FROM job_saved WHERE user_email=? AND  job_id=?");
    $stmt->bind_param("ss",$Email,$jId);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
      return true;
    } else {
      return false;
    }

}
public function checkApplied($Email, $jobId) {
  $stmt = $this->con->prepare("SELECT Status FROM applied_jobs WHERE Applied_By=? AND Job_id=?");
  $stmt->bind_param("ss", $Email, $jobId);
  $stmt->execute();
  $result = $stmt->get_result();

  if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    $status = $row['Status'];
    return array('status' => $status, 'exists' => true);
  } else {
    return array('status' => null, 'exists' => false);
  }
}
public function applyJob($Email, $jobId,$Name) {
  $status = "Pending"; 
  
  $stmt = $this->con->prepare("INSERT INTO applied_jobs (Job_id, Applied_By, Name, Status) VALUES (?, ?, ?, ?)");
  $stmt->bind_param("ssss", $jobId, $Email,$Name,$status); 
  
  if ($stmt->execute()) {
    $this->appliedHistory($Email, $jobId);
      return true;
  } else {
      return "Error while adding"; 
  } 
}
public function chcekCv($Email) {
  $stmt = $this->con->prepare("SELECT COUNT(*) as num_rows FROM cv WHERE email = ?");
  $stmt->bind_param("s", $Email);
  $stmt->execute();
  $result = $stmt->get_result();
  $row = $result->fetch_assoc();
  $num_rows = $row['num_rows'];  
  return $num_rows > 0;
}


public function appliedHistory($Email,$jobId){
  $stmt=$this->con->prepare("SELECT j_category  FROM jobs WHERE j_id =? ");
  $stmt->bind_param("i",$jobId);
  $stmt->execute();
  $result = $stmt->get_result();
 if ($result->num_rows > 0) {
        $row = $result->fetch_assoc();
        $category = $row['j_category'];

        $stmt = $this->con->prepare("SELECT * FROM applied_history WHERE user = ? AND job_category = ?");
        $stmt->bind_param("ss", $Email, $category);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($result->num_rows > 0) {
         
          $stmt = $this->con->prepare("UPDATE applied_history SET countTimes = countTimes + 1 WHERE user = ? AND job_category = ?");
          $stmt->bind_param("ss", $Email, $category);
          $stmt->execute();
      } else {
          
          $stmt = $this->con->prepare("INSERT INTO applied_history (user, job_category, countTimes) VALUES (?, ?, 1)");
          $stmt->bind_param("ss", $Email, $category);
          $stmt->execute();
      }

    }
   
}



public function getAllAppliedJobs($email){
  $stmt=$this->con->prepare("SELECT * FROM applied_jobs WHERE Applied_By=? ORDER BY applied_id   DESC");
  $stmt->bind_param("s",$email);
  $stmt->execute();
  $result = $stmt->get_result();
  $jobs = array();
  while ($row = $result->fetch_assoc()){
    $jobId = $row['Job_id'];
    $jobData = $this->getJobData($jobId);
    $jobData['Status'] = $row['Status']; 
    $jobs[] = $jobData;
  }
  return $jobs;
}

public function statusUpdate($Email, $jobId,$Status){
  $stmt = $this->con->prepare("UPDATE applied_jobs SET Status = ? WHERE Applied_By = ? AND Job_id=?");
     
  $stmt->bind_param("sss",$Status,$Email,$jobId );
  if ($stmt->execute()) {
    return true;
} else {
    return false;
}
}
public function fetchUserInterest($email) {
  $stmt = $this->con->prepare("SELECT  uCategories  FROM user_profile    WHERE uEmail = ?");            
  $stmt->bind_param("s", $email);
  $stmt->execute();
  $result = $stmt->get_result();
  $data = array();
  while ($row = $result->fetch_assoc()) {
      $data[] = $row['uCategories'];
  }
  $concatenatedCategories = implode(',', $data);

  return $concatenatedCategories;

}
public function fetchUserApplied($email) {
  $stmt = $this->con->prepare("SELECT job_category FROM applied_history WHERE user = ? ORDER BY countTimes DESC");            
  $stmt->bind_param("s", $email);
  $stmt->execute();
  $result = $stmt->get_result();
  $data = array();
  while ($row = $result->fetch_assoc()) {
      $data[] = $row['job_category'];
  }

  $concatenatedCategories = implode(',', $data);

  return $concatenatedCategories;
}





}











  








