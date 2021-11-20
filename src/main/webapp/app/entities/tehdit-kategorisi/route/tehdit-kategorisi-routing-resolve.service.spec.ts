jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITehditKategorisi, TehditKategorisi } from '../tehdit-kategorisi.model';
import { TehditKategorisiService } from '../service/tehdit-kategorisi.service';

import { TehditKategorisiRoutingResolveService } from './tehdit-kategorisi-routing-resolve.service';

describe('TehditKategorisi routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TehditKategorisiRoutingResolveService;
  let service: TehditKategorisiService;
  let resultTehditKategorisi: ITehditKategorisi | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(TehditKategorisiRoutingResolveService);
    service = TestBed.inject(TehditKategorisiService);
    resultTehditKategorisi = undefined;
  });

  describe('resolve', () => {
    it('should return ITehditKategorisi returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTehditKategorisi = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTehditKategorisi).toEqual({ id: 123 });
    });

    it('should return new ITehditKategorisi if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTehditKategorisi = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTehditKategorisi).toEqual(new TehditKategorisi());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TehditKategorisi })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTehditKategorisi = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTehditKategorisi).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
