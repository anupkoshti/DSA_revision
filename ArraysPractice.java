import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class ArraysPractice {

    public int largestElement(int[] nums) {
        int maxi = Integer.MIN_VALUE;
        
        for(int i : nums) {
            maxi = Math.max(maxi, i);
        }
        return maxi;
    }

    public int secondLargestElement(int[] nums) {
        Arrays.sort(nums);
        int largest = nums[nums.length-1];
        int secLar = Integer.MIN_VALUE;
        for(int i=nums.length-1;i>=0;i--){
            if(nums[i] != largest) {
                secLar= nums[i];
                break;
            }
        }
        return secLar == Integer.MIN_VALUE ? -1 : secLar ;
    }

    public int secondLargestElement2(int[] nums) {
        int maxi = Integer.MIN_VALUE;
        
        for(int i : nums) {
            maxi = Math.max(maxi, i);
        }

        int secLargest = Integer.MIN_VALUE;

        for(int i : nums) {
            if(i > secLargest && i != maxi) {
                secLargest = i;
            }
        }
        return secLargest == Integer.MIN_VALUE ? -1 : secLargest;
    }


    public int secondLargestElement3(int[] nums) {
        int largest = Integer.MIN_VALUE;
        int secLargest = Integer.MIN_VALUE;

        for(int i : nums) {
            if(i > largest) {
                largest = i;
            }
            else if(i > secLargest && i != largest) {
                secLargest = i;
            }
        }
        return secLargest == Integer.MIN_VALUE ? -1 : secLargest;
    }

    public int findMaxConsecutiveOnes(int[] nums) {
        int currCnt=0, maxi = 0;

        for(int i=0;i<nums.length;i++){
            if(nums[i] == 1) {
                currCnt++;
            }
            else {
                currCnt = 0;
            }
            maxi = Math.max(maxi, currCnt);
        }
        return maxi;
    }


    //left rotate by one
    public void rotateArrayByOne(int[] nums) {
        int temp = nums[0];

        for(int i=1;i<nums.length;i++){
            nums[i-1]=nums[i];
        }
        nums[nums.length-1] = temp;
    }


    //left rotate array by k places 
     public static void rotateArray(int[] nums, int k) {
        int n=nums.length;
        k = k % n;
        int[] temp = new int[k];

        for(int i=0;i<k;i++){
            temp[i]=nums[i];
        }

        for(int i=k;i<nums.length;i++){
            nums[i-k] = nums[i];
        }

        for(int i=n-k;i<n;i++){
            nums[i] = temp[i-n+k];
        }
    }

    public void rotateArray2(int[] nums, int k) {
        int n = nums.length;
        k = k % n;
        
        reverseArray(nums, 0, k-1);
        reverseArray(nums, k, n-1);
        reverseArray(nums, 0, n-1);

    }

    static void reverseArray(int[] arr, int low, int high) {
        while(low < high) {
            int temp = arr[low];
            arr[low] = arr[high];
            arr[high] = temp;
            low++;
            high--;
        }
    }


    //move zeroes to the end

    public void moveZeroes(int[] nums) {
        int j=0;
        for(int i=0;i<nums.length;i++) {
            if(nums[i] != 0) {
                //swap
                int temp = nums[i];
                nums[i] = nums[j];
                nums[j] = temp;
                j++;
            }
        }   
    }


    public int removeDuplicates(int[] nums) {
        int j=0;
        
        for(int i=j+1;i<nums.length;i++){
            if(nums[i] != nums[j]) {
                nums[j+1] = nums[i];
                j++;
            }
        }
        return j;
    }

    //using two for loops
    public int missingNumber1(int[] nums) {
        int N = nums.length;
        for(int i=0; i<=N;i++) {
            int flag=0;
            for(int j : nums) {
                if(j == i){
                    flag=1;
                    break;
                }
            }
            if(flag == 0) return i;
        }
        return -1;
    }

    //using hashing

    public int missingNumber2(int[] nums) {
        int n = nums.length;
        int[] temp = new int[n+1];

        for(int i : nums) {
            temp[i]++;
        }

        for(int i=0;i<temp.length;i++){
            if(temp[i] == 0) return i;
        }
        return -1;
    }

    // using sum
    public int missingNumber3(int[] nums) {
        int n=nums.length;

        int sum1 = n*(n+1)/2;

        int sum2 = 0;
        for(int i: nums) sum2+=i;

        return sum1-sum2;
    }

    public int missingNumber4(int[] nums) {
        int ans= 0;

        for(int i=0;i<=nums.length;i++) ans=ans^i;

        for(int i : nums) ans = ans^i;

        return ans;
    }


    public int[] unionArray(int[] nums1, int[] nums2) {
        TreeSet<Integer> hs = new TreeSet<>();
        
        for(int i : nums1) hs.add(i);
        for(int i : nums2) hs.add(i);


        int[] res = hs.stream().mapToInt(Integer::intValue).toArray();

        return res;

    }


    public int[] unionArray2(int[] nums1, int[] nums2) {
        int idx1 =0, idx2=0;
        
        ArrayList<Integer> arr = new ArrayList<>();

        while(idx1 < nums1.length && idx2 < nums2.length) {
            if(nums1[idx1] < nums2[idx2]) {
                if(arr.isEmpty() || arr.get(arr.size()-1) != nums1[idx1]) {
                    arr.add(nums1[idx1]);
                }
                idx1++;
            }

            else if (nums2[idx2] < nums1[idx1]) {
                if(arr.isEmpty() || arr.get(arr.size()-1) != nums2[idx2]) {
                    arr.add(nums2[idx2]);
                }
                idx2++;
            }
            else {
                if(arr.isEmpty() || arr.get(arr.size()-1) != nums1[idx1]) {
                    arr.add(nums1[idx1]);
                }
                idx1++;
                idx2++;
            }
        }

        while(idx1 < nums1.length) {
           if(arr.isEmpty() || arr.get(arr.size()-1) != nums1[idx1]) {
                arr.add(nums1[idx1]);
            } 
            idx1++;
        }
        while(idx2 < nums2.length) {
           if(arr.isEmpty() || arr.get(arr.size()-1) != nums2[idx2]) {
                arr.add(nums2[idx2]);
            } 
            idx2++;
        }

        int[] res = arr.stream().mapToInt(Integer::intValue).toArray();

        return res;
    }
        
    public int[] intersectionArray(int[] nums1, int[] nums2) {
        int n1 = nums1.length, n2 = nums2.length;

        int idx1=0, idx2=0;
        List<Integer> res = new ArrayList<>();
        while(idx1<n1 && idx2 < n2) {
            if(nums1[idx1] < nums2[idx2]) {
                idx1++;
            }

            else if(nums2[idx2] < nums1[idx1]) {
                idx2++;
            }
            else {
                res.add(nums1[idx1]);
                idx1++;
                idx2++;
            }
        }

        int[] ans = res.stream().mapToInt(Integer::intValue).toArray();
        return ans;
    }



    //brute -> count frequency of each element 
    public int majorityElement1(int[] nums) {
        int n = nums.length;

        for(int i=0; i<n;i++){
            int count =0;

            for(int j=0;j<n;j++){
                if(nums[j] == nums[i]) {
                    count++;
                }
            }
            if(count > n/2) return nums[i];
        }
        return -1;
    }

    //better using hashmap to store frequencies

    public int majorityElement2(int[] nums) {
        int n = nums.length;

        Map<Integer, Integer> mpp = new HashMap<>();

        for(int i=0;i<n;i++){
            mpp.put(nums[i], mpp.getOrDefault(nums[i],0)+1);
        }

        for(Map.Entry<Integer, Integer> entry : mpp.entrySet()) {
            if(entry.getValue() > n/2) return entry.getKey();
        }
        return -1;
    }


    //using moores voting algorithm

    public int majorityElement3(int[] nums) {
        int n=nums.length;
        int element = nums[0];
        int count=1;

        for(int i=1;i<n;i++){
            if(count == 0) {
                count=1;
                element=nums[i];
            }
            else if(nums[i] == element) {
                count++;
            }
            else {
                count--;
            }
        }

        return element;   
    }

    //brute 
    //an element can never be leader if any one element right to it is equal or greater

    
    public List<Integer> leadersBrute(int[] nums) {
        int n = nums.length;
        List<Integer> ans = new ArrayList<>();
        for(int i=0;i<n;i++) {
            boolean isLeader = true;
            for(int j=i+1;j<n;j++) {
                if(nums[j] >= nums[i]) {
                    isLeader = false;
                    break;
                }
            }
            if(isLeader) {
                ans.add(nums[i]);
            }
        }
        return ans;
    }

    //better
    public List<Integer> leaders(int[] nums) {
        int n = nums.length;
        int maxi=nums[n-1];
        List<Integer> res = new ArrayList<>();
        res.add(maxi);
        for(int i=n-2;i>=0;i--){
            if(nums[i] > maxi) {
                res.add(nums[i]);
                maxi=nums[i];
            }
            // maxi = Math.max(maxi, nums[i]);
        }
        Collections.reverse(res);
        return res;
    }



    public int[] rearrangeArray(int[] nums) {
        List<Integer> pos = new ArrayList<>();   
        List<Integer> neg = new ArrayList<>();   

        int[] res = new int[nums.length];
        for(int i : nums) {
            if(i>0) pos.add(i);
            else neg.add(i);
        }

        for(int i=0;i<pos.size();i++){
            res[2*i] = pos.get(i);
            res[2*i+1] = neg.get(i);
        }
        return res;
    }

    //optimal 
    public int[] rearrangeArray2(int[] nums) {
        int posIdx=0, negIdx=1;

        int[] res = new int[nums.length];

        for(int i=0;i<nums.length;i++){
            if(nums[i] > 0) {
                res[posIdx] = nums[i];
                posIdx+=2;
            }
            else {
                res[negIdx] = nums[i];
                negIdx+=2;
            }
        }
        return res;
    }


    //print matrix is spiral order
    public List<Integer> spiralOrder(int[][] matrix) {
        int n=matrix.length, m=matrix[0].length;
        int top=0, bottom=n-1, left=0, right=m-1;
        List<Integer> ans = new ArrayList<>();
        while(left<=right && top<=bottom) {
            //first row
            for(int i=left;i<=right;i++){
                ans.add(matrix[top][i]);
            }
            top++;

            //last col
            for(int i=top;i<=bottom;i++){
                ans.add(matrix[i][right]);
            }
            right--;

            //last row
            if(top <= bottom) {
                for(int i=right;i>=left;i--){
                    ans.add(matrix[bottom][i]);
                }
                bottom--;
            }

            //first col
            if(left <= right) {
                for(int i=bottom;i>=top;i--){
                    ans.add(matrix[i][left]);
                }
                left++;
            }
        }
        return ans;
    }

    public int pascalTriangleI(int r, int c) {
        return findNCR(r-1, c-1);
    }

    static int findNCR(int n, int r) {
        int ans=1;
        for(int i=1;i<=r;i++){
            ans=ans*n;
            ans=ans/i;
            n--;
        }
        return ans;
    }


    //brute 
    //for rth row there will be r elements 
    public int[] pascalTriangleII(int r) {
        List<Integer> res = new ArrayList<>();
        for(int i=1;i<=r;i++){
            res.add(findNCR(r-1, i-1));
        }

        return res.stream().mapToInt(Integer::intValue).toArray();
    }

    public List<Integer> pascalTriangleIIBetter(int r) {
        List<Integer> res = new ArrayList<>();

        res.add(1);

        int temp = 1;
        for(int i=1;i<r;i++){
            temp = temp * (r-i);
            temp = temp / i;
            res.add(temp);
        }
        return res;
        // return res.stream().mapToInt(Integer::intValue).toArray();
    }

    public List<List<Integer>> pascalTriangleIII(int n) {
        List<List<Integer>> ans = new ArrayList<>();

        for(int i=1;i<=n;i++){
            ans.add(pascalTriangleIIBetter(i));
        }
        return ans;
    }

    

    public void rotateMatrix(int[][] matrix) {
        //take transpose

        for(int i=1;i<matrix.length;i++){
            for(int j=0;j<i;j++){
                //swap
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }

        //reverse each row 

        for(int i=0;i<matrix.length;i++) {
            reverseArray(matrix[i], 0, matrix[i].length-1);
        }
    }

    public int[] twoSum(int[] nums, int target) {
        for(int i=0;i<nums.length;i++){
            int more = target - nums[i];
            for(int j=i+1;j<nums.length;j++) {
                if(nums[j] == more) {
                    return new int[] {i,j};
                }
            }
        }
        return new int[] {-1,-1};
    }

    //using hashmap 
    public int[] twoSum2(int[] nums, int target) {
        HashMap<Integer, Integer> mpp = new HashMap<>();

        for(int i=0;i<nums.length;i++) {
            int remaining = target - nums[i];

            if(mpp.containsKey(remaining)) {
                return new int[] {mpp.get(remaining), i};
            }
            mpp.put(nums[i], i);
        }
        return new int[] {-1,-1};
    }


    //sort the array
    public int[] twoSum3(int[] nums, int target) {
        //sort the array

        Arrays.sort(nums);

        int start=0, end=nums.length-1;

        while (start<end) {
            int sum = nums[start] + nums[end];
            if(sum == target) {
                return new int[] {start,end};
            }
            else if(sum<target) {
                start++;
            }
            else {
                end--;
            }
        }
        return new int[] {-1,-1};
    }


    //using three for loops
    public List<List<Integer>> threeSumBrute(int[] nums) {
        HashSet<List<Integer>> hs = new HashSet<>();
        for(int i=0;i<nums.length;i++){
            for(int j=i+1;j<nums.length;j++){
                for(int k=j+1;k<nums.length;k++){
                    if(nums[i]+nums[j]+nums[k] == 0) {
                        List<Integer> temp = new ArrayList<>();
                        temp.add(nums[i]);
                        temp.add(nums[j]);
                        temp.add(nums[k]);
                        Collections.sort(temp);
                        hs.add(temp);
                    }
                }
            }
        }
        return new ArrayList<>(hs);
    }

    public List<List<Integer>> threeSumBetter(int[] nums) {
        HashSet<List<Integer>> hs = new HashSet<>();

        for(int i=0;i<nums.length;i++){
            HashSet<Integer> middle = new HashSet<>();
            for(int j=i+1;j<nums.length;j++){
                int third = -1*(nums[i]+nums[j]);
                if(middle.contains(third)) {
                    //triplet found
                    List<Integer> temp = Arrays.asList(nums[i],nums[j], third);
                    Collections.sort(temp);
                    hs.add(temp);
                }
                middle.add(nums[j]);
            }
        }
        return new ArrayList<>(hs);
    }

    public List<List<Integer>> threeSumOptimal(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        Arrays.sort(nums);
        // int i=0, j=1,k=nums.length-1;

        for(int i=0;i<nums.length;i++) {
            if(i != 0 && nums[i] == nums[i-1]) continue;
            int j=i+1, k=nums.length-1;
            while(j<k) {
                int sum = nums[i]+nums[j]+nums[k];
                if(sum < 0) {
                    j++;
                    //move j forward
                    while(j < k && nums[j] == nums[j-1]) {
                        j++;
                    }
                }
                else if(sum > 0) {
                    k--;
                    while(j<k && nums[k] == nums[k+1]) {
                        k--;
                    }
                }
                else {
                    //found a triplet
                    List<Integer> temp = Arrays.asList(nums[i], nums[j], nums[k]);
                    res.add(temp);
                    j++;
                    //move j forward
                    while(j < k && nums[j] == nums[j-1]) {
                        j++;
                    }
                    k--;
                    while(j<k && nums[k] == nums[k+1]) {
                        k--;
                    }
                }
            }
        }
        return res;
    }

    //four sum brute

    public List<List<Integer>> fourSum(int[] nums, int target) {
        HashSet<List<Integer>> res = new HashSet<>();
        
        for(int i=0;i<nums.length;i++){
            for(int j=i+1;j<nums.length;j++){
                for(int k=j+1;k<nums.length;k++){
                    for(int l=k+1;l<nums.length;l++){
                        if(nums[i]+nums[j]+nums[k]+nums[l] == target) {
                            List<Integer> temp = Arrays.asList(nums[i],nums[j],nums[k],nums[l]);
                            Collections.sort(temp);
                            res.add(temp);
                        }
                    }
                }
            }
        }
        return new ArrayList<>(res);
    }

    //using hashing - O(n^3)
    public List<List<Integer>> fourSumBetter(int[] nums, int target) {
        HashSet<List<Integer>> res = new HashSet<>();

        for(int i=0;i<nums.length;i++){
            for(int j=i+1;j<nums.length;j++){
                HashSet<Integer> hs = new HashSet<>();
                for(int k=j+1;k<nums.length;k++){
                    int fourth = target - (nums[i]+nums[j]+nums[k]);

                    if(hs.contains(fourth)) {
                        //found a quadruplet
                        List<Integer> temp = Arrays.asList(nums[i],nums[j],nums[k],fourth);
                        Collections.sort(temp);
                        res.add(temp);
                    }
                    hs.add(nums[k]);
                }
            }
        }
        return new ArrayList<>(res);
    }

    public List<List<Integer>> fourSumOptimal(int[] nums, int target) {
        Arrays.sort(nums);
        HashSet<List<Integer>> res = new HashSet<>();
        for(int i=0;i<nums.length;i++) {
            if(i>0 && nums[i] == nums[i-1]) continue;
            for(int j=i+1;j<nums.length;j++) {
                if(j>i+1 && nums[j] == nums[j-1]) continue;
                int k=j+1, l=nums.length-1;
                while(k < l) {
                    int sum = nums[i]+nums[j]+nums[k]+nums[l];

                    if(sum > target) {
                        //move back
                        l--;
                        while(k<l && nums[l] == nums[l+1]){
                            l--;
                        }
                    }

                    else if (sum < target) {
                        k++;
                        while(k<l && nums[k] == nums[k-1]){
                            k++;
                        }
                    }
                    else {
                        //quadruple found
                        List<Integer> temp = Arrays.asList(nums[i], nums[j], nums[k], nums[l]);
                        res.add(temp);
                        l--;
                        while(k<l && nums[l] == nums[l+1]){
                            l--;
                        }
                        k++;
                        while(k<l && nums[k] == nums[k-1]){
                            k++;
                        }
                    }
                }
            }
        }
        return new ArrayList<>(res);
    }

    public void sortZeroOneTwo(int[] nums) {
        int count0=0, count1=0, count2=0;
        
        for(int i:nums) {
            if(i==0) count0++;
            else if (i==1) count1++;
            else count2++;
        }

        for(int i=0;i<count0;i++) nums[i]=0;
        for(int i=count0;i<count0+count1;i++) nums[i]=1;
        for(int i=count0+count1;i<count0+count1+count2;i++) nums[i]=2;
    }

    public void sortZeroOneTwoDNF(int[] nums) {
        int low=0, mid=0, high=nums.length-1;

        while(mid <= high) {
            if(nums[mid] == 0) {
                //swap
                int temp=nums[low];
                nums[low]=nums[mid];
                nums[mid]=temp;
                low++;
                mid++;
            }
            else if(nums[mid] == 1) {
                mid++;
            }
            else {
                int temp=nums[high];
                nums[high]=nums[mid];
                nums[mid]=temp;
                high--;
            }

        }
    }


    //maximum subarray sum
    public int maxSubArray(int[] nums) {
        int maxSum=Integer.MIN_VALUE;
        for(int i=0;i<nums.length;i++){
            for(int j=i;j<nums.length;j++) {
                int sum=0;
                for(int k=i;k<=j;k++){
                    sum+=nums[k];
                }
                maxSum=Math.max(maxSum, sum);
            }
        }    
        return maxSum;
    }

    //better
    public int maxSubArrayBetter(int[] nums) {
        int maxSum = Integer.MIN_VALUE;

        for(int i=0;i<nums.length;i++){
            int sum=0;
            for(int j=i;j<nums.length;j++) {
                sum+=nums[j];
                maxSum = Math.max(maxSum, sum);
            }
        }
        return maxSum;
    }

    //optimal

    public int maxSubArrayOptimal(int[] nums) {
        int maxSum = Integer.MIN_VALUE;
        int sum=0;
        for(int i=0;i<nums.length;i++) {
            sum += Math.max(sum+nums[i], nums[i]);
            maxSum = Math.max(maxSum, sum);
        }
        return maxSum;
    
    }

    public static int maxSubArrayOptiaml2(int[] nums) {
        int sum=0;
        int maxSum = Integer.MIN_VALUE;
        int start=-1, ansStart=-1, ansEnd=-1; 
        for(int i=0;i<nums.length;i++){

            if(sum == 0) start=i;
            sum += nums[i];
            if(sum > maxSum) {
                maxSum = sum;
                ansStart=start;
                ansEnd=i;
            }
            if(sum < 0) sum=0;
        }
        System.out.println("Start: " + ansStart + " end: " + ansEnd);
        return maxSum;
    }


    public void nextPermutation(int[] nums) {
        // Your code goes here
        int pivot=-1, n=nums.length;

        for(int i=n-2;i>=0;i--) {
            if(nums[i] < nums[i+1]) {
                pivot=i;
                break;
            }
        }

        if(pivot==-1){
            reverseArray(nums, 0, n-1);
            return;
        }
        //traverse from the end and find out smallest ele greater than pivot
        for(int i=n-1;i>=pivot;i--) {
            if(nums[i] > nums[pivot]) {
                //swap nums[i], nums[pivot]
                int temp=nums[i];
                nums[i]=nums[pivot];
                nums[pivot]=temp;
                break;
            }
        }
        // reverse from pivot+1 to n-1;
        reverseArray(nums, pivot+1, n-1);
    }

    public static void main(String[] args) {
        int[] arr = new int[] {-2, -3, -7, -2, -10, -4};
        System.out.println(maxSubArrayOptiaml2(arr));
    }
}
