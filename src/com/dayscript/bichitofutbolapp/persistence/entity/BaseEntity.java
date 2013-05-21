package com.dayscript.bichitofutbolapp.persistence.entity;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class BaseEntity /*extends Entity*/ implements Serializable {
	public void fillEntityFromJson(JSONObject obj)
	{	
		if(obj==null){return;}
		Iterator<String> keys;
		keys=obj.keys();
		while(keys.hasNext()){
			String k=keys.next().toString();
			Field campo;
			try {
				campo = getClass().getDeclaredField(k);
				campo.setAccessible(true);
				campo.set(this, obj.opt(k));
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				
				e.printStackTrace();
			}
		  
		}
	}
	public static ArrayList<BaseEntity> fillEntitiesFromJson(JSONArray objs, Class clz) throws InstantiationException, IllegalAccessException
	{
		ArrayList<BaseEntity> ret=new ArrayList<BaseEntity>();
		
		for(int i=0;i<objs.length();i++)
		{	BaseEntity entity=(BaseEntity) clz.newInstance();
			JSONObject obj=objs.optJSONObject(i);
			entity.fillEntityFromJson(obj);
			ret.add(entity);
		}
		if(ret.size()==0)
		{
			ret=null;
		}
		return ret;
	}
	
}
