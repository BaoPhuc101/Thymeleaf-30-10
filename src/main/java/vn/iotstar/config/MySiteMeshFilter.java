package vn.iotstar.config;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

public class MySiteMeshFilter extends ConfigurableSiteMeshFilter {

	@Override
	protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
//		builder.addDecoratorPath("/*", "/web.jsp")
//				.addDecoratorPath("/admin/*", "/*")
//				.addExcludedPath("/v1/api/*");
	}
	
}
