package com.github.ttran17.basemods;

public interface IClassTransformer {

	public byte[] transform(String name, String transformedName, byte[] bytes);

}
