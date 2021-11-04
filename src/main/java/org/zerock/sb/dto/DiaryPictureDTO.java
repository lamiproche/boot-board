package org.zerock.sb.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "uuid")  //뭐가 중복인지 기준
public class DiaryPictureDTO {

    private String uuid;
    private String fileName;
    private String savePath;
    private int idx;

    public String getLink() {
        //썸네일 링크
        return savePath+"/s_"+uuid+"_"+fileName;
    }

}
