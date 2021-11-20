import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Onay } from 'app/entities/enumerations/onay.model';
import { IRisk, Risk } from '../risk.model';

import { RiskService } from './risk.service';

describe('Risk Service', () => {
  let service: RiskService;
  let httpMock: HttpTestingController;
  let elemDefault: IRisk;
  let expectedResult: IRisk | IRisk[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RiskService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      adi: 'AAAAAAA',
      aciklama: 'AAAAAAA',
      onayDurumu: Onay.ONAYLANMADI,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Risk', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Risk()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Risk', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          adi: 'BBBBBB',
          aciklama: 'BBBBBB',
          onayDurumu: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Risk', () => {
      const patchObject = Object.assign({}, new Risk());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Risk', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          adi: 'BBBBBB',
          aciklama: 'BBBBBB',
          onayDurumu: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Risk', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRiskToCollectionIfMissing', () => {
      it('should add a Risk to an empty array', () => {
        const risk: IRisk = { id: 123 };
        expectedResult = service.addRiskToCollectionIfMissing([], risk);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(risk);
      });

      it('should not add a Risk to an array that contains it', () => {
        const risk: IRisk = { id: 123 };
        const riskCollection: IRisk[] = [
          {
            ...risk,
          },
          { id: 456 },
        ];
        expectedResult = service.addRiskToCollectionIfMissing(riskCollection, risk);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Risk to an array that doesn't contain it", () => {
        const risk: IRisk = { id: 123 };
        const riskCollection: IRisk[] = [{ id: 456 }];
        expectedResult = service.addRiskToCollectionIfMissing(riskCollection, risk);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(risk);
      });

      it('should add only unique Risk to an array', () => {
        const riskArray: IRisk[] = [{ id: 123 }, { id: 456 }, { id: 40292 }];
        const riskCollection: IRisk[] = [{ id: 123 }];
        expectedResult = service.addRiskToCollectionIfMissing(riskCollection, ...riskArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const risk: IRisk = { id: 123 };
        const risk2: IRisk = { id: 456 };
        expectedResult = service.addRiskToCollectionIfMissing([], risk, risk2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(risk);
        expect(expectedResult).toContain(risk2);
      });

      it('should accept null and undefined values', () => {
        const risk: IRisk = { id: 123 };
        expectedResult = service.addRiskToCollectionIfMissing([], null, risk, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(risk);
      });

      it('should return initial array if no Risk is added', () => {
        const riskCollection: IRisk[] = [{ id: 123 }];
        expectedResult = service.addRiskToCollectionIfMissing(riskCollection, undefined, null);
        expect(expectedResult).toEqual(riskCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
