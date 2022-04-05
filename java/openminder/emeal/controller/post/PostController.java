package openminder.emeal.controller.post;

import lombok.RequiredArgsConstructor;
import openminder.emeal.config.file.FileStorageProperties;
import openminder.emeal.controller.file.FileUploadController;
import openminder.emeal.domain.account.Attendance;
import openminder.emeal.domain.file.Picture;
import openminder.emeal.domain.file.UploadFile;
import openminder.emeal.domain.file.UploadFileResponse;
import openminder.emeal.domain.post.*;
import openminder.emeal.service.account.AccountService;
import openminder.emeal.service.file.FileStorageService;
import openminder.emeal.service.file.PictureStorageService;
import openminder.emeal.service.post.PostService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequiredArgsConstructor
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    final PostService postService;
    final AccountService accountService;
    final PictureStorageService pictureStorageService;
    final FileStorageService fileStorageService;

    final String boundary = "*****";
    final String crlf = "\r\n";
    final String charset = "UTF-8";
    JSONArray result = null;

    @PostMapping("/upload/foodInfo")
    public Long uploadFoodInfo(@RequestBody Post post) {
        postService.uploadPost(post);


        Date date = new Date();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        String yearAndMonth = Integer.toString(year) + '_' + month;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        System.out.println(day);

        Attendance attendance = new Attendance();
        attendance.setUsername(post.getUsername());
        attendance.setYearAndMonth(yearAndMonth);
        attendance.setDay(day);

        List<Attendance> attendances = accountService.selectAttendance(attendance);

        System.out.println("attendances.size(): " + attendances.size());

        if (attendances.size() == 0) accountService.insertAttendance(attendance);

        return post.getPostId();
    }


    @PostMapping("/upload/post")
    public UploadFileResponse uploadPost(@RequestParam("picture") MultipartFile picture, @RequestParam("postId") String postId, @RequestParam("username") String username) {

        int id = Integer.parseInt(postId);

        String pictureName = pictureStorageService.storePicture(picture);
        String pictureDownloadUri = "/downloadPicture/" + pictureName;

        Picture pictureInfo = new Picture(pictureName, pictureDownloadUri, picture.getContentType(), picture.getSize(), (long) id);
        postService.uploadPicture(pictureInfo);

        BufferedReader in = null;
        try {
            URL url = new URL("http://127.0.0.1:5000/upload"); // 호출할 url
            HttpURLConnection con = (HttpURLConnection)url.openConnection();

            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setConnectTimeout(15000);
            con.setRequestMethod("POST");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Cache-Control", "no-cache");
            con.setRequestProperty("Content-Type", "multipart/form-data;charset="+charset+";boundary=" + boundary);

            OutputStream httpConnOutputStream = con.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(httpConnOutputStream, charset), true);

            /* 파일 데이터를 넣는 부분 */
            writer.append("--" + boundary).append(crlf);
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(picture.getName()).append("\"").append(crlf);
            writer.append("Content-Type: ").append(HttpURLConnection.guessContentTypeFromName(picture.getName())).append(crlf);
            writer.append("Content-Transfer-Encoding: binary").append(crlf);
            writer.append(crlf);
            writer.flush();

            File convFile = new File(Objects.requireNonNull(picture.getOriginalFilename()));
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(picture.getBytes());
            fos.close();
            FileInputStream inputStream = new FileInputStream(convFile);
            byte[] buffer = new byte[(int) (convFile).length()];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                httpConnOutputStream.write(buffer, 0, bytesRead);
            }
            httpConnOutputStream.flush();
            inputStream.close();
            writer.append(crlf);
            writer.flush();

            writer.append("--" + boundary + "--").append(crlf);
            writer.close();

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                try {
                    in = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));
                    JSONParser parser = new JSONParser();
                    Object obj = parser.parse(in);
                    System.out.println(obj);
                    result = new JSONArray((String) obj);
                    in.close();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                in = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(in);
                System.out.println(obj);
                result = new JSONArray((String) obj);
                in.close();
            }
            con.disconnect();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(in != null) try { in.close(); } catch(Exception e) { e.printStackTrace(); }
        }

        System.out.println(result);

        /* flask server 에서 return 한 음식 수만큼 for 문 돌리기 */
        for (int i = 0;i < result.length();i++){
            JSONObject element = (JSONObject) result.opt(i);
            String menuName = element.optString("en_label");
            // upload Menu
            Menu menu = new Menu(menuName, (long) id);
            postService.uploadMenu(menu);
            // upload Nutrient
            long calorie = Long.parseLong(element.optString("calorie"));
            long carbohydrate = Long.parseLong(element.optString("carbohydrate"));
            long protein = Long.parseLong(element.optString("protein"));
            long fat = Long.parseLong(element.optString("fat"));
            long sugars = Long.parseLong(element.optString("sugars"));
            long sodium = Long.parseLong(element.optString("sodium"));
            long cholesterol = Long.parseLong(element.optString("cholesterol"));
            long fatty_acid = Long.parseLong(element.optString("fatty_acid"));
            long trans_fat = Long.parseLong(element.optString("trans_fat"));
            Nutrient nutrient = new Nutrient(NutrientType.MEAL, calorie, carbohydrate, protein, fat, sugars, sodium, cholesterol, fatty_acid, trans_fat, menu.getMenuId(), username);
            postService.uploadNutrient(nutrient);
        }


        UploadFileResponse uploadFileResponse = new UploadFileResponse(pictureName, pictureDownloadUri, picture.getContentType(), picture.getSize());
        return uploadFileResponse;

    }

    @PostMapping("update/postAccountInfo")
    public void updatePostAccountInfo(@RequestBody Post post) {
        postService.updatePostAccountInfo(post);
    }

    @GetMapping("downloadPicture/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = pictureStorageService.loadFileAsResource(fileName);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            logger.info("Could not determine file type.");
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(contentType))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//                .body(resource);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);

    }

    @GetMapping("/download/recentPosts")
    public List<Post> findRecentPosts(@RequestParam("numPosts") Long numPosts) {
        return postService.findRecentPosts(numPosts);
    }

    @GetMapping("/download/postCategory")
    public List<Post> findPostByPostType(@RequestParam("category") String postType) {
        return postService.findPosts(postType);
    }

    @GetMapping("/download/userPosts")
    public List<Post> findPostByUsername(@RequestParam("username") String username) {
        return postService.findUserPosts(username);
    }

    @GetMapping("/download/onePost")
    public Post findOnePostByPostId(@RequestParam("postId") Long postId) {
        return postService.findOnePost(postId);
    }

    @GetMapping("download/menuAndNutrient")
    public List<Menu> findMenuByPostId(@RequestParam("postId") Long postId) {
        return postService.findMenusByPostId(postId);
    }

    @GetMapping("download/userNutrients")
    public List<Nutrient> findNutrientByUsername(@RequestParam("username") String username) {
        return postService.findNutrientByUsername(username);
    }




//    @PostMapping("/upload/post/pictures")
//    public List<UploadFileResponse> uploadPictures(@RequestParam("post") Post post, @RequestParam("pictures") MultipartFile[] pictures) {
//
//        postService.uploadPost(post);
//
//        Menu menu = new Menu("Almond", post.getPostId());
//        postService.uploadMenu(menu);
//        Nutrient nutrient = new Nutrient(NutrientType.MEAL, (long)579, (long)21, (long)21, (long)49, (long)4, (long)1, (long)0, (long)3, (long)0, menu.getMenuId());
//        postService.uploadNutrient(nutrient);
//
//        return Arrays.asList(pictures)
//                .stream()
//                .map(picture -> uploadPicture(picture))
//                .collect(Collectors.toList());
//    }
}
