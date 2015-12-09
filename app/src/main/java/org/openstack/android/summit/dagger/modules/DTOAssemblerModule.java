package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.DTOAssembler;
import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
@Module
public class DTOAssemblerModule {
    @Provides
    @Singleton
    IDTOAssembler providesDTOAssembler() {
        return new DTOAssembler();
    }

}
