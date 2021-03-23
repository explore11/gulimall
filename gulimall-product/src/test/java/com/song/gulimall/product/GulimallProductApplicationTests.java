package com.song.gulimall.product;

import com.song.gulimall.product.entity.BrandEntity;
import com.song.gulimall.product.service.BrandService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallProductApplicationTests {
    @Resource
    BrandService brandService;

    @Value("${oss.accessKeyId}")
    private String accessKeyId;

    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${oss.endpoint}")
    private String endpoint;

    @Value("${oss.bucket}")
    private String bucket;

    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    RedissonClient redissonClient;

    @Test
    public void testredissonClient() {
        System.out.println(redissonClient);
    }

    @Test
    public void testRedis() {
        stringRedisTemplate.opsForValue().set("hello", "world" + UUID.randomUUID().toString());

        System.out.println(stringRedisTemplate.opsForValue().get("hello"));
    }


//    @Test
//    public void test() throws FileNotFoundException {
//        // Endpoint以杭州为例，其它Region请按实际情况填写。
//        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
//
//        // 创建OSSClient实例。
//        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
//
//        // 上传文件流。
//        InputStream inputStream = new FileInputStream("D:\\0d40c24b264aa511.jpg");
//        ossClient.putObject(bucket, "99.jpg", inputStream);
//        // 关闭OSSClient。
//        ossClient.shutdown();
//
//        System.out.println("上传完成了.....");
//    }

    @Test
    public void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName("华为");
        brandService.save(brandEntity);
        System.out.println("成功。。。。。");
    }


    @Test
    public void test1() {
        List<Long> list = new ArrayList<>();

        for (Long i = 0L; i < 10L; i++) {
            list.add(0, i);
        }


        System.out.println(Arrays.toString(list.toArray()));
    }


}
