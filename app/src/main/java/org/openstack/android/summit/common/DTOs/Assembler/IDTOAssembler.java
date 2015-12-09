package org.openstack.android.summit.common.DTOs.Assembler;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public interface IDTOAssembler {
    <T,E> E createDTO(T source, Class<E> destinationType);
}
