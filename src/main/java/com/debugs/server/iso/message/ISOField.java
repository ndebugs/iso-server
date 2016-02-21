/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.iso.message;

import com.debugs.messaging.Field;
import com.debugs.messaging.attribute.Filter;
import com.debugs.messaging.attribute.Length;
import com.debugs.messaging.attribute.Required;
import com.debugs.messaging.lang.LObject;
import com.debugs.messaging.type.TObject;
import com.debugs.server.iso.message.attribute.ISOFilter;
import com.debugs.server.iso.message.attribute.ISOLength;
import com.debugs.server.iso.xml.adapter.TypeAdapter;
import com.debugs.server.iso.xml.adapter.ValueAdapter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@XmlType(propOrder={"type", "length", "filter", "value"})
@XmlAccessorType(XmlAccessType.FIELD)
public class ISOField extends Field<Integer> {
    
    @XmlElement
    private ISOLength length;
    
    @XmlElement
    private ISOFilter filter;
    
    @XmlAttribute
    @Override
    public Integer getId() {
        return super.getId();
    }

    @Override
    public void setId(Integer id) {
        super.setId(id);
    }

    @XmlElement
    @XmlJavaTypeAdapter(TypeAdapter.class)
    @Override
    public TObject getType() {
        return super.getType();
    }

    @Override
    public Required getRequired() {
        return null;
    }

    @Override
    public void setRequired(Required required) {}

    @Override
    public ISOLength getLength() {
        return length;
    }

    @Override
    public void setLength(Length length) {
        this.length = (ISOLength) length;
    }
    
    @Override
    public ISOFilter getFilter() {
        return filter;
    }

    @Override
    public void setFilter(Filter filter) {
        this.filter = (ISOFilter) filter;
    }
    
    @XmlElement
    @XmlJavaTypeAdapter(ValueAdapter.class)
    @Override
    public LObject getValue() {
        return super.getValue();
    }
}
