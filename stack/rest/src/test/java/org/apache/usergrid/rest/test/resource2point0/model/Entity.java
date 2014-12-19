/**
 * Created by ApigeeCorporation on 12/4/14.
 */
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.apache.usergrid.rest.test.resource2point0.model;


import java.io.Serializable;
import java.util.*;

import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import static org.apache.usergrid.persistence.Schema.PROPERTY_NAME;


/**
 * Contains a model that can be deconstructed from the api response. This is a base level value that contains the bare
 * minumum of what other classes use. Such as . users or groups.
 */

@XmlRootElement
public class Entity implements Serializable, Map<String,Object> {


    protected Map<String, Object> dynamic_properties = new TreeMap<String, Object>( String.CASE_INSENSITIVE_ORDER );

    ApiResponse response;

    public Entity(){}

    public Entity (Map<String,Object> payload){
        this.putAll(payload);
    }

    public Entity(ApiResponse response){
        this.response = response;

        if(response.getEntities() !=null &&  response.getEntities().size()>=1){
            List<Entity>  entities =  response.getEntities();
            Map<String,Object> entity = entities.get(0);
            this.putAll(entity);
        }
    }

    public UUID getUuid(){
        return  UUID.fromString( (String) get("uuid") );
    }


    public void setUuid( UUID uuid ) {
        put("uuid", uuid);
    }

    //TODO: see if this is needed
    //    @JsonSerialize( include = JsonSerialize.Inclusion.NON_NULL )
    public String getType() {
        return (String) get( "type" );
    }


    public void setType( String type ) {
        put("type",type);
    }

    public Long getCreated() {
        return (Long) get( "created" );
    }


    public void setCreated( Long created ) {
        if ( created == null ) {
            created = System.currentTimeMillis();
        }
        put( "created", created );
    }


    public Long getModified() {
        return (Long) get( "modified" );
    }


    public void setModified( Long modified ) {
        if ( modified == null ) {
            modified = System.currentTimeMillis();
        }
        put( "modified", modified );
    }


    public String getName() {
        Object value = getProperty( PROPERTY_NAME );

        if ( value instanceof UUID ) {
            // fixes existing data that uses UUID in USERGRID-2099
            return value.toString();
        }

        return (String) get( "name" );
    }


    @JsonIgnore
    public Map<String, Object> getProperties() {
        return dynamic_properties;
    }


    public final Object getProperty( String propertyName ) {
        return get( propertyName );
    }



    public Entity addProperty(String key, Object value){
        put(key,value);
        return this;
    }
    public void setProperties( Map<String, Object> properties ) {
        putAll( properties );
    }

    //For the owner , should have different cases that looks at the different types it could be
    protected Entity setResponse(final ApiResponse response, String key) {
        LinkedHashMap linkedHashMap = (LinkedHashMap) response.getData();

        this.putAll((Map<? extends String, ?>) linkedHashMap.get(key));

        return this;
    }

    public Object getMetadata( String key ) {
        return getDataset( "metadata", key );
    }


    public void setMetadata( String key, Object value ) {
        setDataset( "metadata", key, value );
    }


    public void mergeMetadata( Map<String, Object> new_metadata ) {
        mergeDataset( "metadata", new_metadata );
    }


    public void clearMetadata() {
        clearDataset("metadata");
    }


    public <T> T getDataset( String property, String key ) {
        Object md = get( property );
        if ( md == null ) {
            return null;
        }
        if ( !( md instanceof Map<?, ?> ) ) {
            return null;
        }
        @SuppressWarnings( "unchecked" ) Map<String, T> metadata = ( Map<String, T> ) md;
        return metadata.get( key );
    }


    public <T> void setDataset( String property, String key, T value ) {
        if ( key == null ) {
            return;
        }
        Object md = get( property );
        if ( !( md instanceof Map<?, ?> ) ) {
            md = new HashMap<String, T>();
            put( property, md );
        }
        @SuppressWarnings( "unchecked" ) Map<String, T> metadata = ( Map<String, T> ) md;
        metadata.put(key, value);
    }


    public <T> void mergeDataset( String property, Map<String, T> new_metadata ) {
        Object md = get( property );
        if ( !( md instanceof Map<?, ?> ) ) {
            md = new HashMap<String, T>();
            put( property, md );
        }
        @SuppressWarnings( "unchecked" ) Map<String, T> metadata = ( Map<String, T> ) md;
        metadata.putAll( new_metadata );
    }


    public void clearDataset( String property ) {
        remove(property);
    }


    public List<org.apache.usergrid.persistence.Entity> getCollections( String key ) {
        return getDataset( "collections", key );
    }


    public void setCollections( String key, List<org.apache.usergrid.persistence.Entity> results ) {
        setDataset("collections", key, results);
    }


    public List<org.apache.usergrid.persistence.Entity> getConnections( String key ) {
        return getDataset( "connections", key );
    }


    public void setConnections( String key, List<org.apache.usergrid.persistence.Entity> results ) {
        setDataset( "connections", key, results );
    }


    public String toString() {
        return "Entity(" + getProperties() + ")";
    }

    @JsonAnySetter
    public void setDynamicProperty( String key, Object value ) {
        if ( value == null || value.equals( "" ) ) {
            if ( containsKey( key ) ) {
                remove( key );
            }
        }
        else {
            put( key, value );
        }
    }


    @JsonAnyGetter
    public Map<String, Object> getDynamicProperties() {
        return dynamic_properties;
    }

    @Override
    public int size() {
        return getDynamicProperties().size();
    }


    @Override
    public boolean isEmpty() {
        return getDynamicProperties().isEmpty();
    }


    @Override
    public boolean containsKey( final Object key ) {
        return getDynamicProperties().containsKey( key );
    }


    @Override
    public boolean containsValue( final Object value ) {
        return getDynamicProperties().containsValue( value );
    }


    @Override
    public Object get( final Object key ) {
        //All values are strings , so doing the cast here saves doing the cast elsewhere
        return getDynamicProperties().get( key );
    }

    public String getError () {
        return (String) this.get("error").toString();
    }

    public String getErrorCode () {
        return (String) this.get("errorCode").toString();
    }

    public String getErrorDescription () {
        return (String) this.get("errorDescription").toString();
    }

    @Override
    public Object put( final String key, final Object value ) {
        return getDynamicProperties().put( key,value );
    }


    @Override
    public Object remove( final Object key ) {
        return getDynamicProperties().remove( key );
    }


    @Override
    public void putAll( final Map<? extends String, ?> m ) {
        getDynamicProperties().putAll( m );
    }


    @Override
    public void clear() {
        getDynamicProperties().clear();
    }


    @Override
    public Set<String> keySet() {
        return getDynamicProperties().keySet();
    }


    @Override
    public java.util.Collection<Object> values() {
        return getDynamicProperties().values();
    }


    @Override
    public Set<Entry<String, Object>> entrySet() {
        return getDynamicProperties().entrySet();
    }
}