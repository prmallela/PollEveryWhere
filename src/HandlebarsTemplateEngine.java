import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.io.RuntimeIOException;

import spark.ModelAndView;
import spark.TemplateEngine;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.cache.GuavaTemplateCache;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.github.jknack.handlebars.io.TemplateSource;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * Renders HTML from Route output using
 * https://github.com/jknack/handlebars.java.
 * Defaults to the 'templates' directory under the resource path.
 */
public class HandlebarsTemplateEngine extends TemplateEngine {

    private Handlebars handlebars;

    /**
     * Constructs a handlebars template engine
     */
    public HandlebarsTemplateEngine() {
        this("/templates");
    }

    /**
     * Constructs a handlebars template engine
     *
     * @param resourceRoot the resource root
     */
    public HandlebarsTemplateEngine(String resourceRoot) {
        TemplateLoader templateLoader = new ClassPathTemplateLoader();
        templateLoader.setPrefix(resourceRoot);
        templateLoader.setSuffix(null);

        handlebars = new Handlebars(templateLoader);

        // Set Guava cache.
        Cache<TemplateSource, Template> cache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1000).build();

        handlebars = handlebars.with(new GuavaTemplateCache(cache));

        // Customize by registering helpers.
        handlebars.registerHelpers(TimestampHelper.class);
        handlebars.registerHelpers(Paths.class);
    }

    @Override
    public String render(ModelAndView modelAndView) {
        String viewName = modelAndView.getViewName();
        try {
            Template template = handlebars.compile(viewName);
            return template.apply(modelAndView.getModel());
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }
}