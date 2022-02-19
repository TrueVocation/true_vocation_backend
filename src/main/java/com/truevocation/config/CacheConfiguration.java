package com.truevocation.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.truevocation.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.truevocation.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.truevocation.domain.User.class.getName());
            createCache(cm, com.truevocation.domain.Authority.class.getName());
            createCache(cm, com.truevocation.domain.User.class.getName() + ".authorities");
            createCache(cm, com.truevocation.domain.City.class.getName());
            createCache(cm, com.truevocation.domain.City.class.getName() + ".demandProfessionCities");
            createCache(cm, com.truevocation.domain.City.class.getName() + ".schools");
            createCache(cm, com.truevocation.domain.City.class.getName() + ".universities");
            createCache(cm, com.truevocation.domain.City.class.getName() + ".courses");
            createCache(cm, com.truevocation.domain.Favorite.class.getName());
            createCache(cm, com.truevocation.domain.Faculty.class.getName());
            createCache(cm, com.truevocation.domain.Faculty.class.getName() + ".specialties");
            createCache(cm, com.truevocation.domain.Faculty.class.getName() + ".universities");
            createCache(cm, com.truevocation.domain.Specialty.class.getName());
            createCache(cm, com.truevocation.domain.Specialty.class.getName() + ".subjects");
            createCache(cm, com.truevocation.domain.Specialty.class.getName() + ".professions");
            createCache(cm, com.truevocation.domain.University.class.getName());
            createCache(cm, com.truevocation.domain.University.class.getName() + ".contacts");
            createCache(cm, com.truevocation.domain.University.class.getName() + ".favorites");
            createCache(cm, com.truevocation.domain.University.class.getName() + ".comments");
            createCache(cm, com.truevocation.domain.University.class.getName() + ".faculties");
            createCache(cm, com.truevocation.domain.Translation.class.getName());
            createCache(cm, com.truevocation.domain.DemandProfessionCity.class.getName());
            createCache(cm, com.truevocation.domain.Profession.class.getName());
            createCache(cm, com.truevocation.domain.Profession.class.getName() + ".demandProfessionCities");
            createCache(cm, com.truevocation.domain.Profession.class.getName() + ".recommendations");
            createCache(cm, com.truevocation.domain.Profession.class.getName() + ".courses");
            createCache(cm, com.truevocation.domain.Profession.class.getName() + ".specialties");
            createCache(cm, com.truevocation.domain.Contact.class.getName());
            createCache(cm, com.truevocation.domain.Subject.class.getName());
            createCache(cm, com.truevocation.domain.Subject.class.getName() + ".specialties");
            createCache(cm, com.truevocation.domain.Likes.class.getName());
            createCache(cm, com.truevocation.domain.Comments.class.getName());
            createCache(cm, com.truevocation.domain.Comments.class.getName() + ".likes");
            createCache(cm, com.truevocation.domain.Comments.class.getName() + ".commentAnswers");
            createCache(cm, com.truevocation.domain.CommentAnswer.class.getName());
            createCache(cm, com.truevocation.domain.Post.class.getName());
            createCache(cm, com.truevocation.domain.Post.class.getName() + ".likes");
            createCache(cm, com.truevocation.domain.Post.class.getName() + ".favorites");
            createCache(cm, com.truevocation.domain.Post.class.getName() + ".comments");
            createCache(cm, com.truevocation.domain.Course.class.getName());
            createCache(cm, com.truevocation.domain.Course.class.getName() + ".contacts");
            createCache(cm, com.truevocation.domain.Course.class.getName() + ".professions");
            createCache(cm, com.truevocation.domain.School.class.getName());
            createCache(cm, com.truevocation.domain.School.class.getName() + ".portfolios");
            createCache(cm, com.truevocation.domain.Portfolio.class.getName());
            createCache(cm, com.truevocation.domain.Portfolio.class.getName() + ".contacts");
            createCache(cm, com.truevocation.domain.Portfolio.class.getName() + ".achievements");
            createCache(cm, com.truevocation.domain.Portfolio.class.getName() + ".languages");
            createCache(cm, com.truevocation.domain.Portfolio.class.getName() + ".schools");
            createCache(cm, com.truevocation.domain.Language.class.getName());
            createCache(cm, com.truevocation.domain.Language.class.getName() + ".portfolios");
            createCache(cm, com.truevocation.domain.Achievement.class.getName());
            createCache(cm, com.truevocation.domain.ProfTest.class.getName());
            createCache(cm, com.truevocation.domain.ProfTest.class.getName() + ".questions");
            createCache(cm, com.truevocation.domain.ProfTest.class.getName() + ".testResults");
            createCache(cm, com.truevocation.domain.Question.class.getName());
            createCache(cm, com.truevocation.domain.Question.class.getName() + ".answers");
            createCache(cm, com.truevocation.domain.Answer.class.getName());
            createCache(cm, com.truevocation.domain.Answer.class.getName() + ".questions");
            createCache(cm, com.truevocation.domain.TestResult.class.getName());
            createCache(cm, com.truevocation.domain.TestResult.class.getName() + ".answerUsers");
            createCache(cm, com.truevocation.domain.Recommendation.class.getName());
            createCache(cm, com.truevocation.domain.AnswerUser.class.getName());
            createCache(cm, com.truevocation.domain.AppUser.class.getName());
            createCache(cm, com.truevocation.domain.AppUser.class.getName() + ".comments");
            createCache(cm, com.truevocation.domain.AppUser.class.getName() + ".favorites");
            createCache(cm, com.truevocation.domain.AppUser.class.getName() + ".commentAnswers");
            createCache(cm, com.truevocation.domain.AppUser.class.getName() + ".likes");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
