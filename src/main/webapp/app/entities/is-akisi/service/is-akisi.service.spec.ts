import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IIsAkisi, IsAkisi } from '../is-akisi.model';

import { IsAkisiService } from './is-akisi.service';

describe('IsAkisi Service', () => {
  let service: IsAkisiService;
  let httpMock: HttpTestingController;
  let elemDefault: IIsAkisi;
  let expectedResult: IIsAkisi | IIsAkisi[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(IsAkisiService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      konu: 'AAAAAAA',
      aciklama: 'AAAAAAA',
      sonTarih: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          sonTarih: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a IsAkisi', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          sonTarih: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          sonTarih: currentDate,
        },
        returnedFromService
      );

      service.create(new IsAkisi()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a IsAkisi', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          konu: 'BBBBBB',
          aciklama: 'BBBBBB',
          sonTarih: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          sonTarih: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a IsAkisi', () => {
      const patchObject = Object.assign(
        {
          aciklama: 'BBBBBB',
          sonTarih: currentDate.format(DATE_FORMAT),
        },
        new IsAkisi()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          sonTarih: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of IsAkisi', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          konu: 'BBBBBB',
          aciklama: 'BBBBBB',
          sonTarih: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          sonTarih: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a IsAkisi', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addIsAkisiToCollectionIfMissing', () => {
      it('should add a IsAkisi to an empty array', () => {
        const isAkisi: IIsAkisi = { id: 123 };
        expectedResult = service.addIsAkisiToCollectionIfMissing([], isAkisi);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(isAkisi);
      });

      it('should not add a IsAkisi to an array that contains it', () => {
        const isAkisi: IIsAkisi = { id: 123 };
        const isAkisiCollection: IIsAkisi[] = [
          {
            ...isAkisi,
          },
          { id: 456 },
        ];
        expectedResult = service.addIsAkisiToCollectionIfMissing(isAkisiCollection, isAkisi);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a IsAkisi to an array that doesn't contain it", () => {
        const isAkisi: IIsAkisi = { id: 123 };
        const isAkisiCollection: IIsAkisi[] = [{ id: 456 }];
        expectedResult = service.addIsAkisiToCollectionIfMissing(isAkisiCollection, isAkisi);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(isAkisi);
      });

      it('should add only unique IsAkisi to an array', () => {
        const isAkisiArray: IIsAkisi[] = [{ id: 123 }, { id: 456 }, { id: 99712 }];
        const isAkisiCollection: IIsAkisi[] = [{ id: 123 }];
        expectedResult = service.addIsAkisiToCollectionIfMissing(isAkisiCollection, ...isAkisiArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const isAkisi: IIsAkisi = { id: 123 };
        const isAkisi2: IIsAkisi = { id: 456 };
        expectedResult = service.addIsAkisiToCollectionIfMissing([], isAkisi, isAkisi2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(isAkisi);
        expect(expectedResult).toContain(isAkisi2);
      });

      it('should accept null and undefined values', () => {
        const isAkisi: IIsAkisi = { id: 123 };
        expectedResult = service.addIsAkisiToCollectionIfMissing([], null, isAkisi, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(isAkisi);
      });

      it('should return initial array if no IsAkisi is added', () => {
        const isAkisiCollection: IIsAkisi[] = [{ id: 123 }];
        expectedResult = service.addIsAkisiToCollectionIfMissing(isAkisiCollection, undefined, null);
        expect(expectedResult).toEqual(isAkisiCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
