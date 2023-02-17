package mx.lania.g4d.config;

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
            createCache(cm, mx.lania.g4d.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, mx.lania.g4d.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, mx.lania.g4d.domain.User.class.getName());
            createCache(cm, mx.lania.g4d.domain.Authority.class.getName());
            createCache(cm, mx.lania.g4d.domain.User.class.getName() + ".authorities");
            createCache(cm, mx.lania.g4d.domain.Bitacora.class.getName());
            createCache(cm, mx.lania.g4d.domain.Iteracion.class.getName());
            createCache(cm, mx.lania.g4d.domain.Iteracion.class.getName() + ".funcionalidads");
            createCache(cm, mx.lania.g4d.domain.Proyecto.class.getName());
            createCache(cm, mx.lania.g4d.domain.Proyecto.class.getName() + ".participacionProyectos");
            createCache(cm, mx.lania.g4d.domain.Proyecto.class.getName() + ".configuracions");
            createCache(cm, mx.lania.g4d.domain.Proyecto.class.getName() + ".bitacoras");
            createCache(cm, mx.lania.g4d.domain.Proyecto.class.getName() + ".iteracions");
            createCache(cm, mx.lania.g4d.domain.EstatusFuncionalidad.class.getName());
            createCache(cm, mx.lania.g4d.domain.EstatusFuncionalidad.class.getName() + ".funcionalidads");
            createCache(cm, mx.lania.g4d.domain.Etiqueta.class.getName());
            createCache(cm, mx.lania.g4d.domain.Funcionalidad.class.getName());
            createCache(cm, mx.lania.g4d.domain.Funcionalidad.class.getName() + ".users");
            createCache(cm, mx.lania.g4d.domain.Funcionalidad.class.getName() + ".etiquetas");
            createCache(cm, mx.lania.g4d.domain.Funcionalidad.class.getName() + ".atributoFuncionalidads");
            createCache(cm, mx.lania.g4d.domain.Funcionalidad.class.getName() + ".comentarios");
            createCache(cm, mx.lania.g4d.domain.Prioridad.class.getName());
            createCache(cm, mx.lania.g4d.domain.Prioridad.class.getName() + ".funcionalidads");
            createCache(cm, mx.lania.g4d.domain.Comentario.class.getName());
            createCache(cm, mx.lania.g4d.domain.Atributo.class.getName());
            createCache(cm, mx.lania.g4d.domain.Atributo.class.getName() + ".atributoFuncionalidads");
            createCache(cm, mx.lania.g4d.domain.AtributoFuncionalidad.class.getName());
            createCache(cm, mx.lania.g4d.domain.Configuracion.class.getName());
            createCache(cm, mx.lania.g4d.domain.Rol.class.getName());
            createCache(cm, mx.lania.g4d.domain.Rol.class.getName() + ".participacionProyectos");
            createCache(cm, mx.lania.g4d.domain.ParticipacionProyecto.class.getName());
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
