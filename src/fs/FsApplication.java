package fs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import fs.cache.FsCache;



@SpringBootApplication
public class FsApplication {
	public static void main(String[] args) {
		SpringApplication.run(FsApplication.class, args);
		
		FsCache.reloadCache();
	}
}
