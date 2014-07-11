package org.eclipse.fx.code.compensator.editor.hsl.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.fx.code.compensator.editor.ContentTypeProvider;
import org.eclipse.fx.code.compensator.editor.Input;
import org.eclipse.fx.code.compensator.editor.hsl.HSLConfiguration;
import org.eclipse.fx.code.compensator.editor.hsl.HSLRuleBasedPartitionScanner;
import org.eclipse.fx.code.compensator.editor.services.PartitionerFactory;
import org.eclipse.fx.code.compensator.editor.services.SourceViewerConfigurationFactory;
import org.eclipse.fx.code.compensator.hsl.HSLStandaloneSetup;
import org.eclipse.fx.code.compensator.hsl.hSL.Model;
import org.eclipse.fx.code.compensator.hsl.hSL.Partitioner;
import org.eclipse.fx.code.compensator.hsl.hSL.RulePartitioner;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class HSLComponent implements PartitionerFactory, SourceViewerConfigurationFactory {
	private Map<String, Model> contentTypeMappings = new HashMap<>();
	private ResourceSetImpl rs;

	public HSLComponent() {
		HSLStandaloneSetup.doSetup();
		rs = new ResourceSetImpl();
		registerHslConfig(URI.createPlatformPluginURI("/org.eclipse.fx.code.compensator.editor.hsl/xml/xml.hsl", true));
	}
	
	@Override
	public SourceViewerConfiguration createConfiguration(Input<?> input) {
		Model m = getModelForInput(input);
		if( m == null ) {
			throw new IllegalArgumentException("Unsupported input '"+input+"'");	
		}
		
		return new HSLConfiguration(getClass().getClassLoader(), m);
	}
	
	private void registerHslConfig(URI uri) {
		Model m = loadModel(uri);
		for( String t : m.getContentTypes() ) {
			contentTypeMappings.put(t, m);
		}
	}
	
	private Model loadModel(URI uri) {
		Resource resource = rs.getResource(uri, true);
		return (Model) resource.getContents().get(0); 
	}

	@Override
	public boolean applies(Input<?> input) {
		if( input instanceof ContentTypeProvider ) {
			return contentTypeMappings.containsKey(((ContentTypeProvider) input).getContentType());
		}
		return false;
	}
	
	private Model getModelForInput(Input<?> input) {
		if( input instanceof ContentTypeProvider ) {
			System.err.println("SEARCH FOR  " + input);
			return contentTypeMappings.get(((ContentTypeProvider) input).getContentType());
		}
		return null;
	}
	
	private String[] getParitions(Model m) {
		return m.getPartitions().stream().filter((p) -> { return ! p.getName().equals(IDocument.DEFAULT_CONTENT_TYPE); })
				.map((p) -> p.getName())
				.toArray((size) -> new String[size]);
	}

	@Override
	public IDocumentPartitioner createPartitioner(Input<?> input) {
		Model m = getModelForInput(input);
		if( m == null ) {
			throw new IllegalArgumentException("Unsupported input '"+input+"'");	
		}
		
		
		Partitioner partitioner = m.getPartitioner();
		IPartitionTokenScanner scanner = null;
		if( partitioner instanceof RulePartitioner ) {
			scanner = new HSLRuleBasedPartitionScanner(getClass().getClassLoader(), (RulePartitioner) m.getPartitioner());
		}
		
		if( scanner != null ) {
			return new FastPartitioner(scanner, getParitions(m));
		}
		
		throw new IllegalStateException("Unsupported partitioner '"+partitioner+"'");
	}
	
}
