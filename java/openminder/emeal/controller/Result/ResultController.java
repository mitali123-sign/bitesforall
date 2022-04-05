package openminder.emeal.controller.Result;

import lombok.RequiredArgsConstructor;
import openminder.emeal.domain.post.Nutrient;
import openminder.emeal.service.post.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ResultController {

    final PostService postService;

    @GetMapping("/PieChartPage")
    public List<Nutrient> getNutrientChart(@RequestParam("username") String username) {
        return postService.findOneDayNutrientByUsername(username);
    }

}


//
//import lombok.RequiredArgsConstructor;
//import openminder.emeal.controller.post.PostController;
//import openminder.emeal.domain.post.Menu;
//import openminder.emeal.domain.post.Post;
//import openminder.emeal.mapper.account.AccountRepository;
//import org.apache.tomcat.jni.Local;
//import org.springframework.boot.configurationprocessor.json.JSONArray;
//import org.springframework.boot.configurationprocessor.json.JSONException;
//import org.springframework.boot.configurationprocessor.json.JSONObject;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import javax.servlet.http.HttpServletRequest;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.*;
//
//@RequiredArgsConstructor
//public class ResultController {
//
//    final Menu menu;
//    PostController postController;
//    AccountRepository accountRepository;
//
//
//    @GetMapping("/PieChartPage")
//    public JSONArray GetNutrientChart(RequestParam("userName") String userName) throws JSONException {
//
//        //userID 가져오기
//        /*
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userName = authentication.getName();
//         */
//
//        System.out.println("username : "+userName);
//        //해당 사용자의 게시글 찾기
//        List<Post> userPost = postController.findPostByUsername(userName);
//
//        //오늘 날짜 가져오기
//        Calendar cal = Calendar.getInstance();
//        int year = cal.get(cal.YEAR);
//        int month = cal.get(cal.MONTH)+1;
//        int date = cal.get(cal.DATE);
//
//        LocalDate todayDate = LocalDate.of(year, month, date);
//
//        int userPostSize = userPost.size();
//
//        List<Post> getpostList = new List<Post>() {
//            @Override
//            public int size() {
//                return 0;
//            }
//
//            @Override
//            public boolean isEmpty() {
//                return false;
//            }
//
//            @Override
//            public boolean contains(Object o) {
//                return false;
//            }
//
//            @Override
//            public Iterator<Post> iterator() {
//                return null;
//            }
//
//            @Override
//            public Object[] toArray() {
//                return new Object[0];
//            }
//
//            @Override
//            public <T> T[] toArray(T[] a) {
//                return null;
//            }
//
//            @Override
//            public boolean add(Post post) {
//                return false;
//            }
//
//            @Override
//            public boolean remove(Object o) {
//                return false;
//            }
//
//            @Override
//            public boolean containsAll(Collection<?> c) {
//                return false;
//            }
//
//            @Override
//            public boolean addAll(Collection<? extends Post> c) {
//                return false;
//            }
//
//            @Override
//            public boolean addAll(int index, Collection<? extends Post> c) {
//                return false;
//            }
//
//            @Override
//            public boolean removeAll(Collection<?> c) {
//                return false;
//            }
//
//            @Override
//            public boolean retainAll(Collection<?> c) {
//                return false;
//            }
//
//            @Override
//            public void clear() {
//
//            }
//
//            @Override
//            public Post get(int index) {
//                return null;
//            }
//
//            @Override
//            public Post set(int index, Post element) {
//                return null;
//            }
//
//            @Override
//            public void add(int index, Post element) {
//
//            }
//
//            @Override
//            public Post remove(int index) {
//                return null;
//            }
//
//            @Override
//            public int indexOf(Object o) {
//                return 0;
//            }
//
//            @Override
//            public int lastIndexOf(Object o) {
//                return 0;
//            }
//
//            @Override
//            public ListIterator<Post> listIterator() {
//                return null;
//            }
//
//            @Override
//            public ListIterator<Post> listIterator(int index) {
//                return null;
//            }
//
//            @Override
//            public List<Post> subList(int fromIndex, int toIndex) {
//                return null;
//            }
//        };
//
//        //날짜 같은지 확인해서 오늘 날짜 리스트 만들
//        for(int i = userPostSize-1; i >= 0; i++){
//            Post getpost = userPost.get(i);
//            LocalDate localDate = getpost.getInsertTime().toLocalDate();
//            if(todayDate == localDate) {
//                getpostList.add(getpost);
//            }
//            else break;
//
//        }
//
//        int getPostListSize = getpostList.size();
//
//        int sumCarbohydrate = 300;
//        int sumProtein = 200;
//        int sumFat = 300;
//
//        /*
//        for(int i = 0; i < getPostListSize; i++){
//            List<Menu> postmenu = getpostList.get(i).getMenus();
//            int menuSize = postmenu.size();
//
//            for(int j = 0; j < menuSize; j++){
//                Menu Datamenu = postmenu.get(i);
//                sumCarbohydrate += Datamenu.getNutrient().getCarbohydrate();
//                sumProtein += Datamenu.getNutrient().getProtein();
//                sumFat += Datamenu.getNutrient().getFat();
//            }
//
//
//        }
//
//         */
//
//        //json으로 column 각각 carbohydrate, protein, fat으로 PieChart로 보내기
//        JSONArray sendjson = new JSONArray();
//        JSONObject subjson = new JSONObject();
//        subjson.put("carbohydrate", sumCarbohydrate);
//        sendjson.put(subjson);
//        subjson = new JSONObject();
//        subjson.put("protein",sumProtein);
//        sendjson.put(subjson);
//        subjson = new JSONObject();
//        subjson.put("fat",sumFat);
//        sendjson.put(subjson);
//
//
//        return sendjson;
//
//
//    }
////
////    //라인 그래프. 유저들이 먹은 모든 탄수화물, 단백질, 지방 가져오기
//
//    @GetMapping("/LineGraphPage")
//    public JSONArray GetNutrientGraph() throws JSONException {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userId = authentication.getName();
//
//
//        //해당 사용자의 게시글 찾기
//        List<Post> userPost = postController.findPostByUsername(userId);
//        int userPostSize = userPost.size();
//
//
//        //해당 사용자의 게시글들에서 carbonhydrate, protein, fat 값들 각각 ArrayList/List로 만들기
//        //x값 = insertTime, y값 = 탄수화물, 단백질, 지방 순서
//
//        JSONArray sendjson = new JSONArray();
//        JSONObject subjson = new JSONObject();
//
//        Post userpost1 = userPost.get(0);
//        List<Menu> menu = userpost1.getMenus();
//        int sumCarbohydrate = 0;
//        int sumProtein = 0;
//        int sumFat = 0;
//        LocalDateTime initDateTime = userpost1.getInsertTime();
//        LocalDate initDate = initDateTime.toLocalDate();
//
//        int menuSize = menu.size();
//        for(int i = 0; i < menuSize; i++){
//            sumCarbohydrate += menu.get(i).getNutrient().getCarbohydrate();
//            sumProtein += menu.get(i).getNutrient().getProtein();
//            sumFat += menu.get(i).getNutrient().getFat();
//        }
//        subjson.put("x", initDate);
//        subjson.put("y", sumCarbohydrate);
//        subjson.put("y",sumProtein);
//        subjson.put("y",sumFat);
//        sendjson.put(subjson);
//
//
//        for(int i = 1; i < userPostSize-2; i++){
//            Post getuserPost = userPost.get(i);
//
//            LocalDate getDateTime = getuserPost.getInsertTime().toLocalDate();
//            //i가 n>=2인 경우 initDate(날짜비교군)은 n-1번째의 날짜
//            if(i != 1){
//                initDate = userPost.get(i-1).getInsertTime().toLocalDate();
//            }
//            if(getDateTime == initDate) {
//                //날짜가 이전 날짜와 같으면 초기화하지 않고 계산.
//                List<Menu> getmenu = getuserPost.getMenus();
//                int getmenuSize = getmenu.size();
//                for(int j = 0; j < getmenuSize; j++){
//
//                }
//
//            }
//            else {
//                sumCarbohydrate = 0;
//                sumProtein = 0;
//                sumFat = 0;
//            }
//        }
//
//        //json배열 만드는 반복문으로 해당 포스트의 값들 전부 column에 "y" 추가해서 넣기
//
//        //json배열 리턴하
//        return sendjson;
//    }
//
//
//}
//
//
