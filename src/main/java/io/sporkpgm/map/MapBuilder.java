package io.sporkpgm.map;

import io.sporkpgm.Spork;
import io.sporkpgm.module.Module;
import io.sporkpgm.module.builder.Builder;
import io.sporkpgm.module.exceptions.ModuleLoadException;
import io.sporkpgm.module.modules.info.InfoBuilder;
import io.sporkpgm.module.modules.info.InfoModule;
import io.sporkpgm.region.Region;
import io.sporkpgm.region.RegionBuilder;
import io.sporkpgm.region.exception.InvalidRegionException;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MapBuilder {

	private Document document;
	private File folder;

	private InfoModule info;
	private List<Module> modules;
	private List<Region> regions;

	public MapBuilder(Document document, File folder) throws ModuleLoadException, InvalidRegionException {
		this.document = document;
		this.folder = folder;

		Element root = document.getRootElement();
		this.info = (InfoModule) new InfoBuilder(document).build().get(0);

		this.regions = new ArrayList<>();
		if(root.element("regions") != null) {
			this.regions = RegionBuilder.parseSubRegions(root.element("regions"));
		}

		this.modules = Spork.get().getModules(document);
	}

	public Document getDocument() {
		return document;
	}

	public File getFolder() {
		return folder;
	}

	public InfoModule getInfo() {
		return info;
	}

	public String getName() {
		return info.getName();
	}

	public List<Module> getModules() {
		return modules;
	}

	public List<Region> getRegions() {
		return regions;
	}

	public SporkMap getMap() throws ModuleLoadException, InvalidRegionException {
		return new SporkMap(this);
	}

	public static MapBuilder getLoader(String string) {
		List<MapBuilder> loaders = Spork.getMaps();

		for(MapBuilder loader : loaders)
			if(loader.getName().equalsIgnoreCase(string))
				return loader;

		for(MapBuilder loader : loaders)
			if(loader.getName().toLowerCase().contains(string.toLowerCase()))
				return loader;

		return null;
	}

}