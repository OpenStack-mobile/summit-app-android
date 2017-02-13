package org.openstack.android.summit.common.DTOs.Assembler.Converters;


import org.modelmapper.AbstractConverter;
import org.openstack.android.summit.common.DTOs.TeamDTO;

import io.realm.TeamRealmProxy;

/**
 * Created by smarcet on 2/13/17.
 */

public class TeamRealmProxy2TeamDTO extends AbstractConverter<TeamRealmProxy, TeamDTO>
{

    protected TeamDTO convert(TeamRealmProxy source) {
        TeamDTO dto = createDTO();
        dto.setId(source.getId());
        dto.setName(source.getName());

        return dto;
    }

    protected  TeamDTO createDTO(){return new TeamDTO();}

}