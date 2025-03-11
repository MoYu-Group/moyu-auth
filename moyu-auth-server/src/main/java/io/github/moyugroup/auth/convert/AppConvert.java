package io.github.moyugroup.auth.convert;

import io.github.moyugroup.auth.model.vo.AppVO;
import io.github.moyugroup.auth.orm.model.App;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * App 转换器
 * <p>
 * Created by fanfan on 2024/02/01.
 */
@Mapper
public interface AppConvert {

    AppConvert INSTANCE = Mappers.getMapper(AppConvert.class);

    /**
     * App 转换为 AppVO
     *
     * @param source
     * @return
     */
    AppVO appToAppVO(App source);

}
