package com.oracle.jets.spatial252;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import com.oracle.jets.spatial252.service.AdventiveRefuge;
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
@RequestMapping("/api")
class SpatialController {
 
    @Autowired
    private GeometryService service;

    @Autowired
    private AsyncHelper asyncHelper;

    @RequestMapping(
            value = "/directions/actions/get/between",
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    Direction getDirection(
            @RequestParam double org_lat, @RequestParam double org_lng,
            @RequestParam double dst_lat, @RequestParam double dst_lng)
                    throws Spatial252Exception {
        return service.getShortestDirection(
                new Point(org_lat, org_lng), new Point(dst_lat, dst_lng));
    }

    @RequestMapping(
            value = "/refuges/actions/search/nearest",
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    List<RefugeWithDirection> getNearestRefuges(
            @RequestParam double org_lat, @RequestParam double org_lng,
            @RequestParam int limit)
                    throws Spatial252Exception {
        return service.getNearestRefuges(new Point(org_lat, org_lng), limit);
    }

    @RequestMapping(
            value = "/refuges/{id}",
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    RefugeWithDirection getRefuge(
            @RequestParam double org_lat, @RequestParam double org_lng,
            @PathVariable long id)
                    throws Spatial252Exception {
        // TODO 404になるケース
        return service.getRefuge(new Point(org_lat, org_lng), id);
    }

    @RequestMapping(
            value = "/refuges",
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    RefugeWithDirection addRefuge(@RequestBody AdventiveRefuge refuge)
            throws Spatial252Exception {
        SseEventBuilder event =
                SseEmitter.event().name("add_refuge").data(refuge.getId());
        asyncHelper.sendToAllClients(event);
        return service.addRefuge(refuge);
    }

    @RequestMapping(
            value = "/refuges/{id}/actions/disable",
            method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    // TODO: 変更したリソースの情報を返却する
    void disableRefuge(@PathVariable Long id, @RequestParam String reason)
                    throws Spatial252Exception {
        SseEventBuilder event =
                SseEmitter.event().name("disable_refuge").data(reason);
        asyncHelper.sendToAllClients(event);
        service.disableRefuge(id);
    }

    @RequestMapping(
            value = "/prohibitedareas",
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    List<Polygon> getProhibitedArea() throws Spatial252Exception {
        return service.getDisabledArea();
    }

    @RequestMapping(
            value = "/prohibitedareas",
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    void addProhibitedArea(@RequestBody Polygon prohibitedArea) 
                    throws Spatial252Exception {
        SseEventBuilder event =
                SseEmitter.event().name("disable_area").data("msg");
        asyncHelper.sendToAllClients(event);
        service.disable(prohibitedArea);
    }

    @RequestMapping(
            value = "/sseconnect",
            method = RequestMethod.GET)
    public SseEmitter sse() throws IOException {
        return asyncHelper.getSseEmitter();
    }

}
