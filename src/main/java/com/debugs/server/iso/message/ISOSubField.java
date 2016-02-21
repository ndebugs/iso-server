/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.server.iso.message;

import com.debugs.messaging.Field;
import com.debugs.messaging.attribute.Attribute;
import com.debugs.messaging.attribute.Filter;
import com.debugs.messaging.attribute.Length;
import com.debugs.messaging.attribute.Required;
import com.debugs.messaging.lang.LObject;
import com.debugs.messaging.type.TObject;
import com.debugs.server.iso.message.attribute.ISOFilter;
import com.debugs.server.iso.message.attribute.ISOLength;
import com.debugs.server.iso.message.attribute.ISORequired;
import com.debugs.server.iso.xml.adapter.ValueAdapter;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@XmlType(propOrder={"required", "subFilter", "subValue"})
@XmlAccessorType(XmlAccessType.FIELD)
public class ISOSubField extends Field<Integer> {
    
    @XmlElement
    private ISORequired required;
    
    @XmlElement(name="filter")
    private ISOFilter subFilter;
    
    @XmlTransient
    private ISOField parent;

    @XmlTransient
    private final ISOSubFieldValue valueWrapper;

    public ISOSubField() {
        valueWrapper = new ISOSubFieldValue(this);
    }

    @XmlAttribute
    @Override
    public Integer getId() {
        return super.getId();
    }

    @Override
    public void setId(Integer id) {
        super.setId(id);
    }

    @Override
    public TObject getType() {
        return parent.getType();
    }

    @Override
    public void setType(TObject type) {
        parent.setType(type);
    }
    
    @Override
    public ISORequired getRequired() {
        return required;
    }

    @Override
    public void setRequired(Required required) {
        this.required = (ISORequired) required;
    }

    @Override
    public ISOLength getLength() {
        return parent.getLength();
    }

    @Override
    public void setLength(Length length) {
        parent.setLength(length);
    }
    
    @Override
    public ISOFilter getFilter() {
        return parent.getFilter();
    }

    @Override
    public void setFilter(Filter filter) {
        parent.setFilter(filter);
    }

    public ISOFilter getSubFilter() {
        return subFilter;
    }

    public void setSubFilter(ISOFilter filter) {
        this.subFilter = filter;
    }

    @Override
    public LObject getValue() {
        return valueWrapper;
    }

    @Override
    public void setValue(LObject value) {
        parent.setValue(value);
    }
    
    @XmlElement(name="value")
    @XmlJavaTypeAdapter(ValueAdapter.class)
    public LObject getSubValue() {
        return super.getValue();
    }

    public void setSubValue(LObject value) {
        super.setValue(value);
    }
    
    public ISOField getParent() {
        return parent;
    }

    public void setParent(ISOField parent) {
        this.parent = parent;
    }

    @Override
    public List<Attribute> attributes() {
        List<Attribute> list = super.attributes();
        list.add(getSubFilter());
        return list;
    }
}
