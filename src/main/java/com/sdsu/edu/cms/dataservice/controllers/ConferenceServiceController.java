package com.sdsu.edu.cms.dataservice.controllers;

import com.sdsu.edu.cms.common.models.cms.Conference;
import com.sdsu.edu.cms.common.models.cms.Track;
import com.sdsu.edu.cms.common.models.response.ServiceResponse;
import com.sdsu.edu.cms.dataservice.services.ConferenceMgmtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ConferenceServiceController {

    @Autowired
    ConferenceMgmtService conferenceMgmtService;

    @PostMapping("/conferences/create")
    public ServiceResponse createConference(@RequestBody Conference conference){
        return conferenceMgmtService.createNewConference(conference);
    }

    @PostMapping("/conferences/tracks")
    public ServiceResponse addTracks(@RequestBody List<Track> tracks, @RequestParam Map<String, String> map){
        return conferenceMgmtService.addTracksForConference(tracks, map.get("cid"));
    }

    @PostMapping("/conferences/get/name")
    public ServiceResponse getConferenceByName(@RequestParam Map<String, String> map){
        return conferenceMgmtService.getConferenceByName(map.get("cname"));
    }

    @PostMapping("/conferences/get/tracks")
    public ServiceResponse getTracks(@RequestParam Map<String, String> map){
         return conferenceMgmtService.getTrackNames(map.get("id"));
    }

    @PostMapping("/conferences/update")
    public ServiceResponse updateConference(@RequestBody Conference conference, @RequestParam Map<String, String> map){
        return conferenceMgmtService.updateConferenceData(conference, map.get("cid"));
    }

    @PostMapping("/conferences/delete")
    public ServiceResponse deleteConferences(@RequestParam Map<String, String> map){
        String confId = map.get("id");
        return conferenceMgmtService.deleteConference(confId);

    }

    //TODO: Update Tracks -- If track ID exists, update else create new. For keywords, delete all and re-insert.
}
