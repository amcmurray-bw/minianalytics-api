package amcmurray.bw;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import amcmurray.bw.twitterdomainobjects.Mention;
import amcmurray.bw.twitterdomainobjects.MentionDTO;

@Component
public class MentionPresenter {

    private static final DateTimeFormatter dateFormat
            = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    private MentionDTO toDTO(Mention mention) {
        String date = convertDate(mention.getCreatedAt());

        MentionDTO mentionDTO = new MentionDTO(mention.getId(),
                mention.getQueryId(),
                mention.getMentionType(),
                mention.getText(), date,
                mention.getLanguageCode(), mention.getFavouriteCount());

        return mentionDTO;
    }

    private String convertDate(Date date) {

        //convert date to zoned date time, and then format to string
        return ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).format(dateFormat);
    }

    //takes list of mentions, converts them to MentionDTO and returns
    public List<MentionDTO> toDTOs(List<Mention> allMentions) {
        return allMentions
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
