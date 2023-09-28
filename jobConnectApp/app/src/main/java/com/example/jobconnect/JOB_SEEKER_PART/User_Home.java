
package com.example.jobconnect.JOB_SEEKER_PART;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.jobconnect.ADAPTERS.All_Jobs_Adp;
import com.example.jobconnect.Constants.Constants;
import com.example.jobconnect.Modules.JobModel;
import com.example.jobconnect.Modules.RequestHandler;
import com.example.jobconnect.Modules.SessionManager;
import com.example.jobconnect.R;
import com.example.jobconnect.UI_CONTROLLER.MainActivity;
import com.example.jobconnect.Validations.FormatData;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User_Home extends Fragment {

    private String email;
    private TabLayout tabLayout;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchView searchView;
    private All_Jobs_Adp jobAdapter;
    private String[] AllCategories;

    String[] categoryNameArray;



    private List<JobModel> jobList;


    class CategorySimilarity {
        String categoryName;
        double similarityScore;

        public CategorySimilarity(String categoryName, double similarityScore) {
            this.categoryName = categoryName;
            this.similarityScore = similarityScore;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user__home, container, false);

        SessionManager sessionManager = new SessionManager(requireContext());
        if (sessionManager.checkSession()) {
            email = sessionManager.getSessionDetail("useremail");
        } else {
            startActivity(new Intent(getContext(), MainActivity.class));
        }

        tabLayout = view.findViewById(R.id.tab_layout);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        searchView = view.findViewById(R.id.searchView);
        searchView.clearFocus();
        recyclerView = view.findViewById(R.id.showJobs);




        showAllJobs();
        getSuggestionCategories();

        tabLayout.addTab(tabLayout.newTab().setText("All Jobs"));
        tabLayout.addTab(tabLayout.newTab().setText("Suggested"));
        tabLayout.selectTab(tabLayout.getTabAt(0));
        AllCategories = getResources().getStringArray(R.array.allCategories);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        showAllJobs();
                        break;
                    case 1:
                        showSuggestedJobs();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> {
                int selectedTabPosition = tabLayout.getSelectedTabPosition();
                switch (selectedTabPosition) {
                    case 0:
                        showAllJobs();
                        break;
                    case 1:
                        showSuggestedJobs();
                        break;
                }
                swipeRefreshLayout.setRefreshing(false);
            }, 1000);
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        return view;
    }

    private void filterList(String newText) {
        List<JobModel> filteredList=new ArrayList<>();
        for(JobModel job:jobList){
            if(job.getJTitle().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(job);
            }
        }
        if(filteredList.isEmpty()){
            showAllJobs();
        }else{
            jobAdapter.setFilteredJob(filteredList);

        }
    }


    private void getSuggestionCategories() {
        tabLayout.selectTab(tabLayout.getTabAt(1));

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_GET_CATEGORIES,
                response -> {
                    Log.d("rresponse", "showSuggestedJobs: " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.getBoolean("error")) {
                            String interestCategories = jsonObject.getString("InterestCate");
                            String[] interestCategoriesArray = interestCategories.split(",");

                            String appliedCategories = jsonObject.getString("AppliedCat");

                            String[] appliedCategoriesArray = !TextUtils.isEmpty(appliedCategories) ? appliedCategories.split(",") : new String[0];

                            Map<String, Double> similarityAllInterested;
                            Map<String, Double> similarityAllApplied = null;
                            Map<String, Double> similarityAppliedInterested = null;

                            if (appliedCategoriesArray.length == 0) {
                                similarityAllInterested = cosineSimilarity(AllCategories, interestCategoriesArray);
                            } else {
                                similarityAppliedInterested = cosineSimilarity(interestCategoriesArray, appliedCategoriesArray);
                                similarityAllApplied = cosineSimilarity(AllCategories, appliedCategoriesArray);
                                similarityAllInterested = cosineSimilarity(AllCategories, interestCategoriesArray);
                            }

                            List<CategorySimilarity> nonZeroCategories = new ArrayList<>();

                            // Loop through the AllCategories array and add non-zero similarities to the list
                            for (String category : AllCategories) {
                                double similarityInterested = 0;
                                double similarityApplied = 0;
                                double similarityBoth = 0;

                                if (similarityAllInterested != null && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                    similarityInterested = similarityAllInterested.getOrDefault(category, 0.0);
                                }
                                if (similarityAllApplied != null && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                    similarityApplied = similarityAllApplied.getOrDefault(category, 0.0);
                                }
                                if (similarityAppliedInterested != null && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                    similarityBoth = similarityAppliedInterested.getOrDefault(category, 0.0);
                                }

                                double maxSimilarity = Math.max(similarityInterested, Math.max(similarityApplied, similarityBoth));
                                if (maxSimilarity > 0) {
                                    nonZeroCategories.add(new CategorySimilarity(category, maxSimilarity));
                                }
                            }

                            // Sort the nonZeroCategories list in descending order based on similarity score
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                nonZeroCategories.sort((c1, c2) -> Double.compare(c2.similarityScore, c1.similarityScore));
                            }

                            // After the for loop
                            if (nonZeroCategories.isEmpty()) {
                                categoryNameArray = new String[0]; // Empty array when there are no non-zero similarities
                            } else {
                                categoryNameArray = new String[nonZeroCategories.size()];
                                for (int i = 0; i < nonZeroCategories.size(); i++) {
                                    categoryNameArray[i] = nonZeroCategories.get(i).categoryName;
                                }
                            }

                            // Log the final non-zero categories
                            for (String categoryName : categoryNameArray) {
                                Log.d("CategoryNames", categoryName);
                            }
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }, error -> {
            // Handle error
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("email", email);

                return params;
            }
        };

        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
    private Map<String, Double> cosineSimilarity(String[] arr1, String[] arr2) {
        Map<String, Integer> wordFrequencyMap1 = getWordFrequencyMap(arr1);
        Map<String, Integer> wordFrequencyMap2 = getWordFrequencyMap(arr2);
        double dotProduct = 0;
        for (String word : wordFrequencyMap1.keySet()) {
            if (wordFrequencyMap2.containsKey(word)) {
                dotProduct += wordFrequencyMap1.get(word) * wordFrequencyMap2.get(word);
            }
        }
        // Calculate the magnitudes
        double magnitude1 = calculateMagnitude(wordFrequencyMap1);
        double magnitude2 = calculateMagnitude(wordFrequencyMap2);

        Map<String, Double> similarityMap = new HashMap<>();
        for (String word : arr1) {
            double similarity = 0;
            if (wordFrequencyMap2.containsKey(word)) {
                similarity = dotProduct / (magnitude1 * magnitude2);
            }
            similarityMap.put(word, similarity);
        }
        return similarityMap;
    }

    private double calculateMagnitude(Map<String, Integer> wordFrequencyMap) {
        double magnitudeSquared = 0;
        for (int count : wordFrequencyMap.values()) {
            magnitudeSquared += count * count;
        }
        return Math.sqrt(magnitudeSquared);
    }



    private Map<String, Integer> getWordFrequencyMap(String[] arr) {
        Map<String, Integer> wordFrequencyMap = new HashMap<>();
        for (String word : arr) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                wordFrequencyMap.put(word, wordFrequencyMap.getOrDefault(word, 0) + 1);
            }
        }
        return wordFrequencyMap;
    }

    private void showAllJobs() {
        jobList = new ArrayList<>();
        jobAdapter = new All_Jobs_Adp(jobList, requireActivity().getSupportFragmentManager(), getContext(), email);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(jobAdapter);
        getAllJobs();

    }
    private void showSuggestedJobs() {
        jobList = new ArrayList<>();
        jobAdapter = new All_Jobs_Adp(jobList, requireActivity().getSupportFragmentManager(), getContext(), email);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(jobAdapter);
        getSuggestedJobs();
    }


    private void getAllJobs() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.URL_ALL_JOBS,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Log.d("AllJobs", "getAllJobs: "+response);
                        if(!jsonObject.getBoolean("error")){
                            JSONArray jsonArray = jsonObject.optJSONArray("data");
                            parseAndDisplayJobs(jsonArray);


                        }else {
                            Toast.makeText(getContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("User_HOME", "JSON Exception: " + e.getMessage());
                        throw new RuntimeException(e);
                    }

                },
                error -> {
                    Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("user_HOME", "Volley Error: " + error.getMessage());
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);

    }
    private void getSuggestedJobs() {

        JSONArray jsonArray1 = new JSONArray(Arrays.asList(categoryNameArray));
        String categoryNamesJsonString = jsonArray1.toString();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.URL_SUGGESTED_JOBS,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Log.d("SuggestedJobs", "SuggestedJobs: "+response);
                        if(!jsonObject.getBoolean("error")){
                            JSONArray outerArray = jsonObject.optJSONArray("data");
                            for (int i = 0; i < outerArray.length(); i++) {
                                JSONArray innerArray = outerArray.getJSONArray(i);
                                parseAndDisplayJobs(innerArray);

                            }
                        }else {
                            Toast.makeText(getContext(), "error:"+ jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("User_HOME", "JSON Exception: " + e.getMessage());
                        throw new RuntimeException(e);
                    }

                },
                error -> {
                    Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("user_HOME", "Volley Error: " + error.getMessage());
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("category_names", categoryNamesJsonString); // "category_names" is the key to receive the JSON string in PHP
                return params;
            }
        };
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);

    }
    private void parseAndDisplayJobs(JSONArray jsonArray) {
        try {

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                int jId = jsonObject1.optInt("j_id");
                String jTitle = jsonObject1.optString("j_title");
                String jDescription = jsonObject1.optString("j_description");
                String jEmpType = jsonObject1.optString("j_empType");
                String jCategory = jsonObject1.optString("j_category");
                String jExperience = jsonObject1.optString("j_experience");
                String jEducation = jsonObject1.optString("j_education");
                String jName = jsonObject1.optString("j_name");
                String JIndustry = jsonObject1.optString("j_industry");
                String JSalary = jsonObject1.getString("j_salary");
                String postedDate = FormatData.formatDate(jsonObject1.optString("postedDate"));
                String deadline = FormatData.formatDate(jsonObject1.optString("deadline"));
                String Jvacancies = jsonObject1.getString("vacancies");
                String JEmail = jsonObject1.getString("j_email");
                String status = "Not Available";

                JobModel job = new JobModel(jId, jTitle, jDescription, jEmpType, jEducation, jExperience,
                        jCategory, jName, postedDate, deadline, JIndustry, JSalary, Jvacancies, JEmail, status);
                jobList.add(job);
            }
            jobAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("User_HOME", "JSON Exception: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


}