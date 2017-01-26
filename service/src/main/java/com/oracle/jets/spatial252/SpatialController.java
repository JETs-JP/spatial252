package com.oracle.jets.spatial252;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import com.oracle.jets.spatial252.service.AdditionalRefuge;
import com.oracle.jets.spatial252.service.Direction;
import com.oracle.jets.spatial252.service.GeometryService;
import com.oracle.jets.spatial252.service.Point;
import com.oracle.jets.spatial252.service.Polygon;
import com.oracle.jets.spatial252.service.RefugeWithDirection;


// TODO: ロギングはJSON形式で標準出力に出す
/**
 * @author hhayakaw
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/directions")
class SpatialController {
 
    @Autowired
    private GeometryService service;

    @Autowired
    AsyncHelper asyncHelper;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    Direction getDirection(
            @RequestParam double org_lat, @RequestParam double org_lon,
            @RequestParam double dst_lat, @RequestParam double dst_lon)
                    throws Spatial252Exception {
        return service.getShortestDirection(
                new Point(org_lat, org_lon), new Point(dst_lat, dst_lon));
    }

    @RequestMapping(
            value = "/refuges",
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    List<RefugeWithDirection> getNearestRefuges(
            @RequestParam double org_lat, @RequestParam double org_lon,
            @RequestParam int limit)
                    throws Spatial252Exception {
        return service.getNearestRefuges(new Point(org_lat, org_lon), limit);
    }

    @RequestMapping(
            value = "/refuges",
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    void addRefuge(@RequestBody AdditionalRefuge refuge) throws Spatial252Exception {
        SseEventBuilder event =
                SseEmitter.event().name("add_refuge").data(refuge.getId());
        asyncHelper.sendToAllClients(event);
        service.addRefuge(refuge);
    }

    @RequestMapping(
            value = "/refuges",
            method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    void disableRefuge(@RequestParam Long id) throws Spatial252Exception {
        SseEventBuilder event =
                SseEmitter.event().name("disable_refuge").data("msg");
        asyncHelper.sendToAllClients(event);
        service.disableRefuge(id);
    }

    @RequestMapping(
            value = "/area",
            method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    void disable(@RequestBody Polygon disableArea) 
                    throws Spatial252Exception {
        SseEventBuilder event =
                SseEmitter.event().name("disable_area").data("msg");
        asyncHelper.sendToAllClients(event);
        service.disable(disableArea);
    }

    @RequestMapping(
            value = "/area",
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    List<Polygon> getDisabledArea() throws Spatial252Exception {
        return service.getDisabledArea();
    }

    @RequestMapping(
            value = "/sseconnect",
            method = RequestMethod.GET)
    public SseEmitter sse() throws IOException {
        return asyncHelper.getSseEmitter();
    }

}
